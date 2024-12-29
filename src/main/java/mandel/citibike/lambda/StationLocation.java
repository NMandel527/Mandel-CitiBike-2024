package mandel.citibike.lambda;

import mandel.citibike.json.Station;

public class StationLocation {
    public double lat;
    public double lon;
    public String stationName;
    public String stationId;

    public StationLocation(Station closest) {
        this.lat = closest.lat;
        this.lon = closest.lon;
        this.stationName = closest.name;
        this.stationId = closest.station_id;
    }
}
