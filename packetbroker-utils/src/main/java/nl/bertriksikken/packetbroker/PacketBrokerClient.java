package nl.bertriksikken.packetbroker;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class PacketBrokerClient {

    private static final Logger LOG = LoggerFactory.getLogger(PacketBrokerClient.class);
    private IPacketBrokerRestApi restApi;

    PacketBrokerClient(IPacketBrokerRestApi restApi, PacketBrokerConfig config) {
        this.restApi = Preconditions.checkNotNull(restApi);
    }

    public static final PacketBrokerClient create(PacketBrokerConfig config) {
        LOG.info("Creating new REST client for URL '{}' with timeout {}", config.getUrl(), config.getTimeout());
        OkHttpClient httpClient = new OkHttpClient().newBuilder().callTimeout(config.getTimeout()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(config.getUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).client(httpClient).build();
        IPacketBrokerRestApi restApi = retrofit.create(IPacketBrokerRestApi.class);
        return new PacketBrokerClient(restApi, config);
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
