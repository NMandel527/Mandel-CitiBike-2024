package mandel.citibike.map;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import org.jxmapviewer.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import java.util.List;

public class MapFrame extends JFrame {
    private final MapController controller;
    private final JTextField from;
    private final JTextField to;

    public MapFrame() {
        setTitle("CitiBike Map");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RoutePainter routePainter = new RoutePainter();
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        JXMapViewer mapViewer = new JXMapViewer();

        controller = new MapController(this, mapViewer, routePainter, waypointPainter);

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                controller.setPosition(x, y);
            }
        });

        from = new JTextField();
        from.setEditable(false);
        setFromText("From: Click on the map");

        to = new JTextField();
        to.setEditable(false);
        setToText("To: Click on the map");

        JButton map = new JButton("Map");

        map.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.findStations();
            }
        });

        JButton clear = new JButton("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.add(from);
        panel.add(to);
        panel.add(map);
        panel.add(clear);

        add(panel, BorderLayout.SOUTH);
        add(mapViewer, BorderLayout.CENTER);

        List<Painter<JXMapViewer>> painters = List.of(routePainter, waypointPainter);
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(compoundPainter);
    }

    public void setFromText(String text) {
        from.setText(text);
    }

    public void setToText(String text) {
        to.setText(text);
    }
}