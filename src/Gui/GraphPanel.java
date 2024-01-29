package Gui;

import Parser.EquationParser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {

    private final int LEFT = 20;
    private final int RIGHT = 390;
    private final int TOP = 10;
    private final int BOTTOM = 380;

    private final int RESOLUTION = 200;
    private final int POINT_WIDTH = 4;

    private double xMin, xMax, yMin, yMax;

    private double xScale; // in units pixels/x value
    private double yScale; //in units pixels/y value

    ArrayList<DoublePoint> orderedPairs;
    int[] exactXPoints = new int[RESOLUTION];
    int[] exactYPoints = new int[RESOLUTION];

    private boolean drawingY;

    public GraphPanel() {
        orderedPairs = null;
    }

    public void updateRegion(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        try {
            xScale = (RIGHT - LEFT) / (xMax - xMin);
            yScale = (BOTTOM - TOP) / (yMax - yMin);
        } catch(Exception ignored) {}
    }

    public void updateDots(ArrayList<DoublePoint> orderedPairs) {
        this.orderedPairs = orderedPairs;
    }

    public void updateYFunction(EquationParser yParser) {
        drawingY = !(yParser == null);
        if(drawingY) {
            ArrayList<DoublePoint> exactSolutions = new ArrayList<>();
            double x = xMin;
            double increment = (xMax - xMin) / RESOLUTION;
            for(int idx = 0; idx < RESOLUTION; idx++) {
                exactXPoints[idx] = convertX(x);
                exactYPoints[idx] = convertY(yParser.eval(x, 0));
                x += increment;
            }

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //draw axes and labels
        g2d.drawLine(LEFT, TOP, LEFT, BOTTOM);
        g2d.drawLine(LEFT, BOTTOM, RIGHT, BOTTOM);
        g2d.drawString("y", 10, 10);
        g2d.drawString("x", 390, 393);

        //plot points
        if(orderedPairs != null) {
            for(DoublePoint p : orderedPairs) {
                plotPoint(g2d, p.x, p.y);
            }
        }

        //plot exact solution
        g2d.drawPolyline(exactXPoints, exactYPoints, RESOLUTION);
    }

    private void plotPoint(Graphics2D g2d, double x, double y) {
        int convertedX = convertX(x);
        int convertedY = convertY(y);

        Color before = g2d.getColor();
        g2d.setColor(Color.RED);
        g2d.fillOval(convertedX - (POINT_WIDTH / 2), convertedY - (POINT_WIDTH / 2), POINT_WIDTH, POINT_WIDTH);
        g2d.setColor(before);
    }

    private int convertX(double x) {
        return (int) Math.round(LEFT + (x-xMin)*xScale);
    }

    private int convertY(double y) {
        return (int) Math.round(BOTTOM - (y-yMin)*yScale);
    }
}
