package mandel.citibike;

import mandel.citibike.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClosestStationTest {

    @Test
    void getNumBikesAvailable() {

        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations collectionInfo = service.getStationInformation().blockingGet();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);
        int numBikes = closestStation.getNumBikesAvailable("77e31650-e86a-4b06-b2de-b92df2a7a7f5");

        // then
        assertNotNull(numBikes);
    }

    @Test
    void getNumDocksAvailable() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations collectionInfo = service.getStationInformation().blockingGet();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);
        int numSlots = closestStation.getNumDocksAvailable("77e31650-e86a-4b06-b2de-b92df2a7a7f5");

        // then
        assertNotNull(numSlots);
    }

    @Test
    void findClosestStationWithBikes() {

        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        double lon = -73.886751;
        double lat = 40.74719;

        Stations collectionInfo = service.getStationInformation().blockingGet();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);
        Station closest = closestStation.findClosestStationWithBikes(lat, lon);

        // then
        assertNotNull(closest);
        assertEquals("79 St & Roosevelt Ave", closest.name);
    }

    @Test
    void findClosestStationWithSlots() {

        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        double lon = -73.886751;
        double lat = 40.74719;

        Stations collectionInfo = service.getStationInformation().blockingGet();
        Stations collectionStatus = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collectionInfo, collectionStatus);
        Station closest = closestStation.findClosestStationWithSlots(lat, lon);

        // then
        assertNotNull(closest);
        assertEquals("79 St & Roosevelt Ave", closest.name);
    }
}