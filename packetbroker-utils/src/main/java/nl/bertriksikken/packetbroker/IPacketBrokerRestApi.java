package nl.bertriksikken.packetbroker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPacketBrokerRestApi {

    @GET("/api/v2/gateways")
    public Call<List<GatewayInfo>> getAllGateways();
    
}
