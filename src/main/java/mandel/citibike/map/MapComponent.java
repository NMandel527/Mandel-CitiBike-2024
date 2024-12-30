package mandel.citibike.map;

import org.jxmapviewer.*;
import org.jxmapviewer.input.*;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class MapComponent extends JComponent {

    public MapComponent(JXMapViewer mapViewer) {
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
    }
}
