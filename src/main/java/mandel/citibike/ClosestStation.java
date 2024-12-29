package mandel.citibike;

import mandel.citibike.json.*;

public class ClosestStation {
    private final Stations stationInfo;
    private Stations stationStatus;

    public ClosestStation (Stations stationInfo) {
        this.stationInfo = stationInfo;
    }

    public ClosestStation(Stations stationInfo, Stations stationStatus) {
        this.stationInfo = stationInfo;
        this.stationStatus = stationStatus;
    }

    public int getStatus(String stationId, boolean bikes) {
        int available = 0;
        for (Station station : stationStatus.data.stations) {
            if (station.station_id.equals(stationId)) {
                available = bikes ? station.num_bikes_available : station.num_docks_available;
                break;
            }
        }
        return available;
    }

    public Station findClosestStation(double lat, double lon, boolean withBikes) {
        Station closestStation = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Station station : stationInfo.data.stations) {
            double distance = Math.sqrt(((lat - station.lat) * (lat - station.lat))
                    + ((lon - station.lon) * (lon - station.lon)));
            int bikesOrSlots = numBikesOrSlots(station, withBikes);
            if (distance < minDistance && bikesOrSlots > 0) {
                minDistance = distance;
                closestStation = station;
            }
        }

        return closestStation;
    }

    public int numBikesOrSlots(Station station, boolean bikes) {
        int num = 0;
        for (Station s : stationStatus.data.stations) {
            if (s.station_id.equals(station.station_id)) {
                num = bikes ? s.num_bikes_available : s.num_docks_available;
                break;
            }
        }
        return num;
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
