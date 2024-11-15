package nl.bertriksikken.packetbroker;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class PacketBrokerClient {

    private static final Logger LOG = LoggerFactory.getLogger(PacketBrokerClient.class);
    private final IPacketBrokerRestApi restApi;

    PacketBrokerClient(IPacketBrokerRestApi restApi) {
        this.restApi = Objects.requireNonNull(restApi);
    }

    public static PacketBrokerClient create(PacketBrokerConfig config) {
        Duration timeout = config.getTimeout();
        LOG.info("Creating new REST client for URL '{}' with timeout {}", config.url(), timeout);
        OkHttpClient httpClient = new OkHttpClient().newBuilder().connectTimeout(timeout).readTimeout(timeout)
                .writeTimeout(timeout).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.url())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).client(httpClient).build();
        IPacketBrokerRestApi restApi = retrofit.create(IPacketBrokerRestApi.class);
        return new PacketBrokerClient(restApi);
    }

    public List<GatewayInfo> getAllGateways() throws IOException {
        Response<List<GatewayInfo>> response = restApi.getAllGateways().execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            LOG.warn("Got error: {}", response.errorBody().string());
            return Collections.emptyList();
        }
    }

}
