package nl.bertriksikken.packetbroker;

import java.time.Duration;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public final class PacketBrokerConfig {

    @JsonProperty("url")
    private String url = "https://mapper.packetbroker.net/";

    @JsonProperty("timeout")
    private int timeoutSec = 20;

    public String getUrl() {
        return url;
    }

    public Duration getTimeout() {
        return Duration.ofSeconds(timeoutSec);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{url=%s,timeoutSec=%d}", url, timeoutSec);
    }

}
