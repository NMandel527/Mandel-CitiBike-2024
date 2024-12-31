package mandel.citibike.map;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import mandel.citibike.lambda.*;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class MapController {
    private final JLabel from;
    private final JLabel to;
    private final RoutePainter routePainter;
    private final WaypointPainter<Waypoint> waypointPainter;
    private final MapComponent view;
    private GeoPosition fromPosition = null;
    private GeoPosition toPosition = null;
    private GeoPosition start;
    private GeoPosition end;

    public MapController(JLabel from, JLabel to, RoutePainter routePainter,
                         WaypointPainter waypointPainter, MapComponent view) {
        this.from = from;
        this.to = to;
        this.routePainter = routePainter;
        this.waypointPainter = waypointPainter;
        this.view = view;
    }

    public void findStations() {
        if (fromPosition != null && toPosition != null) {
            LambdaService service = new LambdaServiceFactory().getService();
            Location from = new Location(fromPosition.getLatitude(), fromPosition.getLongitude());
            Location to = new Location(toPosition.getLatitude(), toPosition.getLongitude());
            CitiBikeRequest request = new CitiBikeRequest(from, to);
            Disposable disposable = service.getClosestStations(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(SwingSchedulers.edt())
                    .subscribe(
                            response -> {
                                start = new GeoPosition(response.start.lat, response.start.lon);
                                end = new GeoPosition(response.end.lat, response.end.lon);
                                updateRouteAndWaypoints();
                            },
                            Throwable::printStackTrace
                    );
        }
    }

    private void updateRouteAndWaypoints() {
        List<GeoPosition> route = new ArrayList<>();
        route.add(fromPosition);
        route.add(start);
        route.add(end);
        route.add(toPosition);

        routePainter.setTrack(route);

        waypointPainter.setWaypoints(Set.of(
                new DefaultWaypoint(fromPosition),
                new DefaultWaypoint(start),
                new DefaultWaypoint(end),
                new DefaultWaypoint(toPosition)
        ));

        view.getMapViewer().zoomToBestFit(
                Set.of(fromPosition, start, end, toPosition),
                1.0
        );
    }

    public void setPosition(int x, int y) {
        Point2D.Double point = new Point2D.Double(x, y);
        GeoPosition clickedPosition = view.getMapViewer().convertPointToGeoPosition(point);

        if (fromPosition == null) {
            fromPosition = clickedPosition;
            from.setText(fromPosition.getLatitude() + ", " + fromPosition.getLongitude());
        } else if (toPosition == null) {
            toPosition = clickedPosition;
            to.setText(toPosition.getLatitude() + ", " + toPosition.getLongitude());
        }
    }

    public void clear() {
        routePainter.setTrack(new ArrayList<>());
        waypointPainter.setWaypoints(Set.of());

        from.setText("From: Click on the map");
        to.setText("To: Click on the map");

        fromPosition = null;
        toPosition = null;
        start = null;
        end = null;

        GeoPosition nyc = new GeoPosition(40.7478, -73.9852);
        view.getMapViewer().setZoom(4);
        view.getMapViewer().setAddressLocation(nyc);
    }

    public void drawRoutes() {
        view.drawRoutes(routePainter, waypointPainter);
    }
}