package nl.bertriksikken.packetbroker.export;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import nl.bertriksikken.packetbroker.GatewayInfo.EAntennaPlacement;

/**
 * Flattened CSV version of gatewayinfo.
 */
@JsonPropertyOrder({ "id", "eui", "tenant", "online", "latitude", "longitude", "altitude", "antenna" })
public final class GatewayInfoCsv {

    // jackson constructor
    private GatewayInfoCsv() {
    }

    // public constructor
    public GatewayInfoCsv(String id, String eui, String tenantId) {
        this();
        this.id = id;
        this.eui = eui;
        this.tenant = tenantId;
    }

    public void setStatus(boolean online) {
        this.online = online;
    }

    public void setAntenna(double latitude, double longitude, double altitude, EAntennaPlacement antennaPlacement) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.antenna = antennaPlacement;
    }

    // basic IDs
    @JsonProperty("id")
    public String id = "";

    @JsonProperty("eui")
    public String eui = "";

    @JsonProperty("tenant")
    public String tenant = "";

    // status
    @JsonProperty("online")
    public boolean online = false;

    // antenna
    @JsonProperty("latitude")
    public Double latitude = Double.NaN;
    @JsonProperty("longitude")
    public Double longitude = Double.NaN;
    @JsonProperty("altitude")
    public Double altitude = Double.NaN;
    @JsonProperty("antenna")
    public EAntennaPlacement antenna = EAntennaPlacement.UNKNOWN;

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "{id=%s,eui=%s,tenant=%s,online=%s,latitude=%f,longitude=%f, altitude=%f,antenna=%s}", id, eui, tenant,
                online, latitude, longitude, altitude, antenna);
    }

}
