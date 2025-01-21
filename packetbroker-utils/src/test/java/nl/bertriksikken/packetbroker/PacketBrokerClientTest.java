package nl.bertriksikken.packetbroker;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import nl.bertriksikken.geojson.FeatureCollection;
import nl.bertriksikken.geojson.FeatureCollection.Feature;
import nl.bertriksikken.geojson.GeoJsonGeometry;
import nl.bertriksikken.packetbroker.export.GatewayInfoCsv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class PacketBrokerClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(PacketBrokerClientTest.class);

    public static void main(String[] args) throws IOException {
        PacketBrokerClientTest test = new PacketBrokerClientTest();
        test.run();
    }

    private void run() throws IOException {
        LOG.info("Starting PacketBrokerClientTest");
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        PacketBrokerConfig config = new PacketBrokerConfig();
        try (PacketBrokerClient client = PacketBrokerClient.create(config)) {
            // get single gateway
            GatewayInfo info = client.getGatewayDetails("000013", "ttn", "bertrik-ttig-1");
            LOG.info("Single gateway: {}", mapper.writeValueAsString(info));

            // total gateways
            List<GatewayInfo> gateways = client.getAllGateways();
            LOG.info("Total gateways: {}", gateways.size());

            // Online gateways
            List<GatewayInfo> onlineGateways = gateways.stream().filter(GatewayInfo::online).toList();
            LOG.info("Online gateways: {}", onlineGateways.size());

            // gateways with location
            List<GatewayInfo> gwsWithLocation = onlineGateways.stream().filter(g -> g.location().isValid()).toList();
            LOG.info("Online gateways with location: {}", gwsWithLocation.size());

            // TTN gateways with location
            List<GatewayInfo> ttnGateways = gwsWithLocation.stream().filter(g -> g.tenantId().equals("ttn")).toList();
            LOG.info("Online TTN gateways with location: {}", ttnGateways.size());

            // analyse tenants
            LOG.info("Gateways with location, by tenant:");
            analyzeTenants(gwsWithLocation);

            // write online GWs with location to CSV
            writeCsv(gwsWithLocation, new File("all_gateways.csv"));

            // write online GWs with location as geojson
            writeGeojson(gwsWithLocation, new File("all_gateways.geojson"));
        }
    }

    private void writeGeojson(List<GatewayInfo> gateways, File file) throws IOException {
        List<GatewayInfo> filtered = gateways.stream().filter(g -> g.location().isValid()).toList();

        ObjectMapper mapper = new ObjectMapper();
        FeatureCollection featureCollection = new FeatureCollection();
        for (GatewayInfo gatewayInfo : filtered) {
            GeoJsonGeometry geometry = new GeoJsonGeometry.Point(gatewayInfo.location().latitude(),
                    gatewayInfo.location().longitude());
            Feature feature = new Feature(geometry);

            // feature properties
            feature.addProperty("id", gatewayInfo.id());
            feature.addProperty("tenantID", gatewayInfo.tenantId());
            feature.addProperty("eui", gatewayInfo.eui());
            feature.addProperty("altitude", gatewayInfo.location().altitude());
            feature.addProperty("online", gatewayInfo.online());
            feature.addProperty("antennaPlacement", gatewayInfo.antennaPlacement().toString());
            feature.addProperty("antennaCount", gatewayInfo.antennaCount());

            featureCollection.add(feature);
        }
        mapper.writeValue(file, featureCollection);
    }

    private void writeCsv(List<GatewayInfo> gateways, File file) throws IOException {
        // convert into GatewayInfoCsv
        List<GatewayInfoCsv> csvGateways = new ArrayList<>();
        for (GatewayInfo gw : gateways) {
            if (gw.location().isValid()) {
                GatewayInfoCsv csvGw = new GatewayInfoCsv(gw.id(), gw.eui(), gw.tenantId());
                csvGw.setAntenna(gw.location().latitude(), gw.location().longitude(), gw.location().altitude(),
                        gw.antennaPlacement());
                csvGw.setStatus(gw.online());
                csvGateways.add(csvGw);
            }
        }
        CsvMapper mapper = new CsvMapper();
        FormatSchema schema = mapper.schemaFor(GatewayInfoCsv.class).withHeader();
        try (SequenceWriter writer = mapper.writer(schema).writeValues(file)) {
            writer.writeAll(csvGateways);
        }
    }

    private void analyzeTenants(List<GatewayInfo> gateways) {
        Map<String, Integer> uniqueTenants = new HashMap<>();
        for (GatewayInfo info : gateways) {
            int count = uniqueTenants.getOrDefault(info.tenantId(), 0);
            uniqueTenants.put(info.tenantId(), count + 1);
        }
        for (Entry<String, Integer> entry : uniqueTenants.entrySet()) {
            LOG.info("Tenant {}: {}", entry.getKey(), entry.getValue());
        }
    }

}
