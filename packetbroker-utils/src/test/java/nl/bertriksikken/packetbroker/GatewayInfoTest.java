package nl.bertriksikken.packetbroker;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.bertriksikken.packetbroker.GatewayInfo.EAntennaPlacement;

public final class GatewayInfoTest {
    
    @Test
    public void testToString() {
        GatewayInfo info = new GatewayInfo();
        Assert.assertNotNull(info.toString());
    }
    
    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = getClass().getClassLoader().getResource("gatewayinfo.json");
        GatewayInfo info = mapper.readValue(url, GatewayInfo.class);
        
        Assert.assertEquals("000013", info.netId);
        Assert.assertEquals("adra", info.tenantId);
        Assert.assertEquals("adra-tn-porthmadog", info.id);
        Assert.assertEquals("eu1.cloud.thethings.industries", info.clusterId);
        Assert.assertEquals("2022-08-28T11:03:07.154661Z", info.updatedAt);
        Assert.assertEquals(EAntennaPlacement.OUTDOOR, info.antennaPlacement);
        Assert.assertEquals(1, info.antennaCount);
        Assert.assertEquals(true, info.online);
    }

}
