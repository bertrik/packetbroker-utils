package nl.bertriksikken.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.bertriksikken.geojson.FeatureCollection;
import nl.bertriksikken.geojson.FeatureCollection.Feature;
import nl.bertriksikken.geojson.GeoJsonGeometry;
import nl.bertriksikken.packetbroker.GatewayInfo;
import nl.bertriksikken.packetbroker.PacketBrokerClient;
import nl.bertriksikken.packetbroker.PacketBrokerConfig;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class GatewayGeoJsonWriter {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayGeoJsonWriter.class);

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Converts packet broker gateway list to geojson file.");
            System.err.println("  Usage: GatewayGeoJsonWriter <file>");
            return;
        }
        File file = new File(args[0]);

        PropertyConfigurator.configure("log4j.properties");
        GatewayGeoJsonWriter writer = new GatewayGeoJsonWriter();
        writer.write(file);
    }

    private void write(File file) throws IOException {
        LOG.info("Starting GeoJsonWriter");
        PacketBrokerConfig config = new PacketBrokerConfig();
        try (PacketBrokerClient client = PacketBrokerClient.create(config)) {
            // get all gateways
            List<GatewayInfo> gateways = client.getAllGateways();
            LOG.info("Total gateways: {}", gateways.size());

            // limit to actually online gateways
            gateways = gateways.stream().filter(GatewayInfo::online).toList();

            // write as geojson
            writeGeojson(gateways, file);
        }
        LOG.info("done");
    }

    private void writeGeojson(List<GatewayInfo> gateways, File file) throws IOException {
        LOG.info("Writing geojson to '{}'", file.getAbsolutePath());

        // include only gateways that have a location
        List<GatewayInfo> filtered = gateways.stream().filter(g -> g.location().isValid()).toList();
        LOG.info("Gateways with location: {}", filtered.size());

        ObjectMapper mapper = new ObjectMapper();
        FeatureCollection featureCollection = new FeatureCollection();
        for (GatewayInfo gatewayInfo : filtered) {
            GeoJsonGeometry geometry = new GeoJsonGeometry.Point(gatewayInfo.location().latitude(),
                    gatewayInfo.location().longitude());
            Feature feature = new Feature(geometry);

            // feature properties
            feature.addProperty("netID", gatewayInfo.netId());
            feature.addProperty("tenantID", gatewayInfo.tenantId());
            feature.addProperty("id", gatewayInfo.id());
            feature.addProperty("eui", gatewayInfo.eui());
            feature.addProperty("altitude", gatewayInfo.location().altitude());
            feature.addProperty("online", gatewayInfo.online());
            feature.addProperty("antennaPlacement", gatewayInfo.antennaPlacement().toString());
            feature.addProperty("antennaCount", gatewayInfo.antennaCount());

            featureCollection.add(feature);
        }
        mapper.writeValue(file, featureCollection);
    }

}
