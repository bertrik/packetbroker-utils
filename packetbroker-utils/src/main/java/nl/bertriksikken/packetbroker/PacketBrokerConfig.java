package nl.bertriksikken.packetbroker;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public record PacketBrokerConfig(@JsonProperty("url") String url, @JsonProperty("timeout") int timeoutSec) {

    // sensible defaults
    public PacketBrokerConfig() {
        this("https://mapper.packetbroker.net/", 30);
    }

    public Duration getTimeout() {
        return Duration.ofSeconds(timeoutSec);
    }
}
