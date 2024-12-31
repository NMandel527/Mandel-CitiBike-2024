package mandel.citibike.map;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import org.jxmapviewer.viewer.*;

public class MapFrame extends JFrame {
    private final MapController controller;

    public MapFrame() {
        setTitle("CitiBike Map");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RoutePainter routePainter = new RoutePainter();
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();

        MapComponent component = new MapComponent();

        JLabel from = new JLabel();
        from.setText("From: Click on the map");

        JLabel to = new JLabel();
        to.setText("To: Click on the map");

        controller = new MapController(from, to, routePainter, waypointPainter, component);

        component.getMapViewer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                controller.setPosition(x, y);
            }
        });

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
        add(component, BorderLayout.CENTER);

        controller.drawRoutes();
    }
}