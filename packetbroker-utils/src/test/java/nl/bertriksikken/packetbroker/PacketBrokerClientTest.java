package nl.bertriksikken.packetbroker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class PacketBrokerClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(PacketBrokerClient.class);

    public static void main(String[] args) throws IOException {
        PacketBrokerClientTest test = new PacketBrokerClientTest();
        test.run();
    }

    private void run() throws IOException {
        LOG.info("Starting PacketBrokerClientTest");

        PacketBrokerConfig config = new PacketBrokerConfig();
        PacketBrokerClient client = PacketBrokerClient.create(config);
        List<GatewayInfo> gateways = client.getAllGateways();

        // total gateways
        LOG.info("Got {} total gateways", gateways.size());

        // online gateways
        List<GatewayInfo> online = gateways.stream().filter(g -> g.online).collect(Collectors.toList());
        LOG.info("Got {} total gateways online", online.size());

        // online with location
        List<GatewayInfo> onlineWithLocation = online.stream().filter(g -> g.location.isValid())
                .collect(Collectors.toList());
        LOG.info("Got {} total gateways online with location", onlineWithLocation.size());

        // TTN gateways, online, with location
        List<GatewayInfo> ttnGateways = gateways.stream().filter(g -> g.tenantId.equals("ttn"))
                .filter(g -> g.location.isValid()).collect(Collectors.toList());
        LOG.info("Online TTN gateways with location: {}", ttnGateways.size());

        // analyse tenants
        LOG.info("Gateways that are online, have a location, by tenant:");
        analyzeTenants(onlineWithLocation);

        // write TTN geojson
        writeGeojson(ttnGateways, new File("ttn_gateways.json"));
    }

    private void writeGeojson(List<GatewayInfo> gateways, File file) throws IOException {
        List<GatewayInfo> filtered = gateways.stream().filter(g -> g.location.isValid()).collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("type", "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        root.set("features", features);
        for (GatewayInfo gatewayInfo : filtered) {
            ObjectNode feature = mapper.createObjectNode();
            features.add(feature);
            feature.put("type", "Feature");

            ObjectNode geometry = mapper.createObjectNode();
            feature.set("geometry", geometry);
            geometry.put("type", "Point");
            ArrayNode coordinates = mapper.createArrayNode();
            geometry.set("coordinates", coordinates);
            coordinates.add(gatewayInfo.location.longitude);
            coordinates.add(gatewayInfo.location.latitude);

            // feature properties
            ObjectNode properties = mapper.createObjectNode();
            feature.set("properties", properties);
            properties.put("id", gatewayInfo.id);
            properties.put("tenantID", gatewayInfo.tenantId);
            properties.put("eui", gatewayInfo.eui);
            properties.put("altitude", gatewayInfo.location.altitude);
            properties.put("online", gatewayInfo.online);
            properties.put("antennaPlacement", gatewayInfo.antennaPlacement.toString());
            properties.put("antennaCount", gatewayInfo.antennaCount);
        }

        mapper.writeValue(file, root);
    }

    private void analyzeTenants(List<GatewayInfo> gateways) {
        Map<String, Integer> uniqueTenants = new HashMap<>();
        for (GatewayInfo info : gateways) {
            int count = uniqueTenants.getOrDefault(info.tenantId, 0);
            uniqueTenants.put(info.tenantId, count + 1);
        }
        for (Entry<String, Integer> entry : uniqueTenants.entrySet()) {
            LOG.info("Tenant {}: {}", entry.getKey(), entry.getValue());
        }
    }

}
