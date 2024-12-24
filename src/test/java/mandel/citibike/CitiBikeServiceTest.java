package mandel.citibike;

import mandel.citibike.json.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitiBikeServiceTest {

    @Test
    void stationInformation()
    {
        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        //when
        Stations collection = service.getStationInformation().blockingGet();
        Station station = collection.data.stations[0];

        //then
        assertNotNull(station.station_id);
        assertNotNull(station.name);
        assertNotEquals(0, station.lon);
        assertNotEquals(0, station.lat);
    }

    @Test
    void stationStatus()
    {
        //given
        CitiBikeService service = new CitiBikeServiceFactory().getService();

        //when
        Stations collection = service.getStationStatus().blockingGet();
        Station station = collection.data.stations[3];

        //then
        assertNotEquals(0, station.num_bikes_available);
        assertNotEquals(0, station.num_docks_available);
    }
}