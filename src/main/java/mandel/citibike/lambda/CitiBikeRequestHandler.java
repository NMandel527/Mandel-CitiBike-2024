package mandel.citibike.lambda;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;

import mandel.citibike.*;
import mandel.citibike.json.*;

public class CitiBikeRequestHandler implements RequestHandler
        <APIGatewayProxyRequestEvent, CitiBikeRequestHandler.CitiBikeResponse> {

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);

        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations collectionInfo = service.getStationInformation().blockingGet();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);

        Station closestFrom = closestStation.findClosestStationWithBikes(request.from.lat, request.from.lon);
        Station closestTo = closestStation.findClosestStationWithSlots(request.to.lat, request.to.lon);

        StationLocation start = new StationLocation();
        StationLocation end = new StationLocation();
        populateStationLocation(start, closestFrom);
        populateStationLocation(end, closestTo);

        return new CitiBikeResponse(request.from, start, end, request.to);
    }

    private void populateStationLocation(StationLocation startOrEnd, Station closest) {
        startOrEnd.lat = closest.lat;
        startOrEnd.lon = closest.lon;
        startOrEnd.stationName = closest.name;
        startOrEnd.stationId = closest.station_id;
    }

    record CitiBikeRequest(Location from, Location to
    ) {

    }

    record CitiBikeResponse(Location from, StationLocation start, StationLocation end, Location to
    ) {
    }
}
