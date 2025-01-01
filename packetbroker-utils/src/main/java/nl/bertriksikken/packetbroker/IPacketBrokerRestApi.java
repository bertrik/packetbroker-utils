package nl.bertriksikken.packetbroker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * See <a href="https://packetbroker.net/getting-started/api/">Packet broker API</a>
 */
public interface IPacketBrokerRestApi {

    @GET("/api/v2/gateways")
    Call<List<GatewayInfo>> getAllGateways();
    
}
