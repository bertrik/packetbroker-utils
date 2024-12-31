package nl.bertriksikken.packetbroker;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.bertriksikken.packetbroker.GatewayInfo.EAntennaPlacement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class GatewayInfoTest {
    
    @Test
    public void testToString() {
        GatewayInfo info = new GatewayInfo();
        Assertions.assertNotNull(info.toString());
    }
    
    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = getClass().getClassLoader().getResource("gatewayinfo.json");
        GatewayInfo info = mapper.readValue(url, GatewayInfo.class);
        
        Assertions.assertEquals("000013", info.netId());
        Assertions.assertEquals("adra", info.tenantId());
        Assertions.assertEquals("adra-tn-porthmadog", info.id());
        Assertions.assertEquals("eu1.cloud.thethings.industries", info.clusterId());
        Assertions.assertEquals("2022-08-28T11:03:07.154661Z", info.updatedAt());
        Assertions.assertEquals(EAntennaPlacement.OUTDOOR, info.antennaPlacement());
        Assertions.assertEquals(1, info.antennaCount());
        Assertions.assertTrue(info.online());
    }

}
