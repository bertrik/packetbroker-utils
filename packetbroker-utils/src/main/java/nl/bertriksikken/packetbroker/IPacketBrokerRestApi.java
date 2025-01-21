package nl.bertriksikken.packetbroker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * See <a href="https://packetbroker.net/getting-started/api/">Packet broker API</a>
 */
public interface IPacketBrokerRestApi {

    @GET("/api/v2/gateways")
    Call<List<GatewayInfo>> getAllGateways();

    @GET("/api/v2/gateways/{query}")
    Call<GatewayInfo> getGatewayDetails(@Path("query") String query);

}
