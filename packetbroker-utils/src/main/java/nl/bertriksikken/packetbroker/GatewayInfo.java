package nl.bertriksikken.packetbroker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

/**
 * See <a href="https://packetbroker.net/getting-started/api/">API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class GatewayInfo {

    @JsonProperty("netID")
    public String netId = "";

    @JsonProperty("tenantID")
    public String tenantId = "";

    @JsonProperty("id")
    public String id = "";

    @JsonProperty("eui")
    public String eui = "";

    @JsonProperty("clusterID")
    public String clusterId = "";

    @JsonProperty("updatedAt")
    public String updatedAt = "";

    @JsonProperty("location")
    public Location location = new Location();

    @JsonProperty("antennaPlacement")
    public EAntennaPlacement antennaPlacement = EAntennaPlacement.UNKNOWN;

    @JsonProperty("antennaCount")
    public int antennaCount = 0;

    @JsonProperty("online")
    public boolean online = false;

    @JsonProperty("rxRate")
    public double rxRate = Double.NaN;

    @JsonProperty("txRate")
    public double txRate = Double.NaN;

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%f,%f}", netId, tenantId, id, eui, clusterId,
                updatedAt, location, antennaPlacement, antennaCount, online, rxRate, txRate);
    }

    public record Location(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude,
                            @JsonProperty("altitude") double altitude, @JsonProperty("accuracy") double accuracy) {
        // no-arg jackson constructor
        Location() {
            this(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        }

        public boolean isValid() {
            return Double.isFinite(latitude) && Double.isFinite(longitude) && Double.isFinite(altitude);
        }
    }

    public enum EAntennaPlacement {
        UNKNOWN, //
        INDOOR, OUTDOOR,
    }

}
