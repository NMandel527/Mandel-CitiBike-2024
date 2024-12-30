package mandel.citibike.map;

import org.jxmapviewer.*;
import org.jxmapviewer.input.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.util.List;

public class MapComponent extends JComponent {
    private final JXMapViewer mapViewer;

    public MapComponent(JXMapViewer mapViewer) {
        this.mapViewer = mapViewer;
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);

        GeoPosition nyc = new GeoPosition(40.7478, -73.9852);
        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(nyc);

        MouseInputListener listener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(listener);
        mapViewer.addMouseMotionListener(listener);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
        repaint();
    }

    public void drawRoutes(RoutePainter routePainter, WaypointPainter<Waypoint> waypointPainter) {
        List<Painter<JXMapViewer>> painters = List.of(routePainter, waypointPainter);
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(compoundPainter);
        repaint();
    }
}
