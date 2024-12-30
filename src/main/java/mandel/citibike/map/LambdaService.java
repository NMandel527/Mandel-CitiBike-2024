package mandel.citibike.map;

import io.reactivex.rxjava3.core.Single;
import mandel.citibike.lambda.*;
import retrofit2.http.*;

public interface LambdaService {
    @POST("/")
    Single<CitiBikeResponse> getClosestStations(@Body CitiBikeRequest request);
}