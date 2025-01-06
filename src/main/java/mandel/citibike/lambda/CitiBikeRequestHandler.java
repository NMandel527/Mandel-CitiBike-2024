package mandel.citibike.lambda;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;

import mandel.citibike.*;
import mandel.citibike.json.*;

public class CitiBikeRequestHandler implements RequestHandler
        <APIGatewayProxyRequestEvent, CitiBikeResponse> {

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);

        CitiBikeService service = new CitiBikeServiceFactory().getService();
        StationsCache stations = new StationsCache();

        Stations collectionInfo = stations.getStations();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);

        Station closestFrom = closestStation.findClosestStationWithBikes(request.from.lat, request.from.lon);
        Station closestTo = closestStation.findClosestStationWithSlots(request.to.lat, request.to.lon);

        StationLocation start = new StationLocation(closestFrom);
        StationLocation end = new StationLocation(closestTo);

        return new CitiBikeResponse(request.from, start, end, request.to);
    }
}