package mandel.citibike;

import io.reactivex.rxjava3.core.Single;
import mandel.citibike.json.*;
import retrofit2.http.GET;

public interface CitiBikeService {
    @GET("/gbfs/en/station_information.json")
    Single<Stations> getStationInformation();

    @GET("gbfs/en/station_status.json")
    Single<Stations> getStationStatus();
}
