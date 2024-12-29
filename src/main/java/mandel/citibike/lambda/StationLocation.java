package mandel.citibike.lambda;

import mandel.citibike.json.Station;

public class StationLocation {
    double lat;
    double lon;
    String stationName;
    String stationId;

    public StationLocation(Station closest) {
        this.lat = closest.lat;
        this.lon = closest.lon;
        this.stationName = closest.name;
        this.stationId = closest.station_id;
    }
}
