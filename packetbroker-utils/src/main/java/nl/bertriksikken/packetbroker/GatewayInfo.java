package nl.bertriksikken.packetbroker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * See <a href="https://packetbroker.net/getting-started/api/">API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GatewayInfo(@JsonProperty("netID") String netId,
                          @JsonProperty("tenantID") String tenantId,
                          @JsonProperty("id") String id,
                          @JsonProperty("eui") String eui,
                          @JsonProperty("clusterID") String clusterId,
                          @JsonProperty("updatedAt") String updatedAt,
                          @JsonProperty("location") Location location,
                          @JsonProperty("antennaPlacement") EAntennaPlacement antennaPlacement,
                          @JsonProperty("antennaCount") int antennaCount,
                          @JsonProperty("online") boolean online,
                          @JsonProperty("rxRate") Double rxRate,
                          @JsonProperty("txRate") Double txRate) {

    public GatewayInfo {
        netId = Objects.requireNonNullElse(netId, "");
        tenantId = Objects.requireNonNullElse(tenantId, "");
        id = Objects.requireNonNullElse(id, "");
        eui = Objects.requireNonNullElse(eui, "");
        clusterId = Objects.requireNonNullElse(clusterId, "");
        updatedAt = Objects.requireNonNullElse(updatedAt, "");
        location = Objects.requireNonNullElse(location, new Location());
        antennaPlacement = Objects.requireNonNullElse(antennaPlacement, EAntennaPlacement.UNKNOWN);
        rxRate = Objects.requireNonNullElse(rxRate, Double.NaN);
        txRate = Objects.requireNonNullElse(txRate, Double.NaN);
    }

    public GatewayInfo() {
        this(null, null, null, null, null, null, null, null, 0, false, null, null);
    }

    public enum EAntennaPlacement {
        UNKNOWN, //
        INDOOR, OUTDOOR,
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

}
