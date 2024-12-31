package mandel.citibike.map;

import mandel.citibike.lambda.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaServiceTest {
    @Test
    void getClosestStations() {

        // given
        LambdaService service = new LambdaServiceFactory().getService();

        Location from = new Location(40.8211, -73.9359);
        Location to = new Location(40.7471, -73.8867);

        // when
        CitiBikeRequest request = new CitiBikeRequest(from, to);
        CitiBikeResponse citiBikeResponse = service.getClosestStations(request).blockingGet();

        // then
        assertEquals("Lenox Ave & W 146 St", citiBikeResponse.start.stationName);
        assertEquals("79 St & Roosevelt Ave", citiBikeResponse.end.stationName);
    }
 }