package nl.bertriksikken.packetbroker.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import nl.bertriksikken.packetbroker.GatewayInfo.EAntennaPlacement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class GatewayInfoCsvTest {

    @Test
    public void testSerialize() throws JsonProcessingException {
        GatewayInfoCsv info = new GatewayInfoCsv("id", "eui", "tenant");
        info.setStatus(true);
        info.setAntenna(53.7, 4.7, 0.0, EAntennaPlacement.INDOOR);
        Assertions.assertNotNull(info.toString());

        CsvMapper mapper = new CsvMapper();
        String csvData = mapper.writerWithSchemaFor(GatewayInfoCsv.class).writeValueAsString(info);
        Assertions.assertEquals("id,eui,tenant,true,53.7,4.7,0.0,INDOOR\n", csvData);
    }

}
