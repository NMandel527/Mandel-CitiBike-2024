package mandel.citibike;

import mandel.citibike.json.*;

public class ClosestStation {
    private final Stations stations;

    public ClosestStation(Stations stations) {
        this.stations = stations;
    }

    public int getStatus(String stationId, boolean bikes) {
        int available = 0;
        for (Station station : stations.data.stations) {
            if (station.station_id.equals(stationId)) {
                available = bikes ? station.num_bikes_available : station.num_docks_available;
                break;
            }
        }
        return available;
    }

    public Station findClosestStation(double lat, double lon, boolean withBikes) {
        Station closestStation = null;
        double minDistance = Double.MAX_VALUE;

        for (Station station : stations.data.stations) {
            double distance = Math.sqrt(Math.pow(station.lat - lat, 2) + Math.pow(station.lon - lon, 2));
            int bikesOrSlots = withBikes ? station.num_bikes_available : station.num_docks_available;
            if (distance < minDistance && bikesOrSlots > 0) {
                minDistance = distance;
                closestStation = station;
            }
        }

        return closestStation;
    }

    public int getNumBikesAvailable(String stationId) {
        return getStatus(stationId, true);
    }

    public int getNumDocksAvailable(String stationId) {
        return getStatus(stationId, false);
    }

    public Station findClosestStationWithBikes(double lat, double lon) {
        return findClosestStation(lat, lon, true);
    }

    public Station findClosestStationWithSlots(double lat, double lon) {
        return findClosestStation(lat, lon, false);
    }
}
