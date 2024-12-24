package mandel.citibike;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import mandel.citibike.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClosestStationTest {

    @Test
    void getNumBikesAvailable() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations collection = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collection);
        int numBikes = closestStation.getNumBikesAvailable("77e31650-e86a-4b06-b2de-b92df2a7a7f5");

        // then
        assertNotNull(numBikes);
    }

    @Test
    void getNumDocksAvailable() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations collection = service.getStationStatus().blockingGet();

        // when
        ClosestStation closestStation = new ClosestStation(collection);
        int numSlots = closestStation.getNumDocksAvailable("77e31650-e86a-4b06-b2de-b92df2a7a7f5");

        // then
        assertNotNull(numSlots);
    }



    @Test
    void findClosestStationWithBikes() {

        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        double lat = 40.652513;
        double lon = -74.008905;

        Disposable disposable = service.getStationInformation()
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        collection -> {
                            // when
                            ClosestStation closestStation = new ClosestStation(collection);
                            Station closest = closestStation.findClosestStationWithBikes(lat, lon);

                            // then
                            assertEquals("41 St & 3 Ave", closest.name);
                        },
                        Throwable::printStackTrace);
    }

    @Test
    void findClosestStationWithSlots() {

        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        double lat = 40.652513;
        double lon = -74.008905;

        Disposable disposable = service.getStationInformation()
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        collection -> {
                            // when
                            ClosestStation closestStation = new ClosestStation(collection);
                            Station closest = closestStation.findClosestStationWithSlots(lat, lon);

                            // then
                            assertEquals("41 St & 3 Ave", closest.name);
                        },
                        Throwable::printStackTrace);
    }
}