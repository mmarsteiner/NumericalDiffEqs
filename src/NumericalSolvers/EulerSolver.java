package NumericalSolvers;

import Gui.DoublePoint;
import Gui.NumericalSolverGUI;
import Parser.EquationParser;

import java.awt.*;
import java.util.ArrayList;

public class EulerSolver implements NumericalSolver {
    @Override
    public void solve(NumericalSolverGUI gui) {
        gui.getData(gui);
        EquationParser dydxParser = new EquationParser(gui.getDydxInput());
        EquationParser exactParser = null;
        boolean yInputted = true;
        try {
            exactParser = new EquationParser(gui.getExactSolutionInput());
        } catch (Exception e) {
            System.out.println("Not using exact solution because it failed to parse");
        }
        int n = 0;
        int iterations = Integer.parseInt(gui.getIterationsInput());
        double h = Double.parseDouble(gui.getHInput());
        double xn = Double.parseDouble(gui.getX0Input());
        double yn = Double.parseDouble(gui.getY0Input());
        double yMin = 0;
        double yMax = yn;
        StringBuilder resultBuilder = new StringBuilder("|n  |x_n       |y_n       |y         |Abs Error |% Error   | |f(x_n,y_n)|y_(n+1)   |");
        ArrayList<DoublePoint> points = new ArrayList<>();
        while(n <= iterations) {
            double f = dydxParser.eval(xn, yn);
            double yn1 = yn + (h * f);
            double y = Double.NaN;
            double error = Double.NaN;
            double relError = Double.NaN;
            try {
                if (exactParser != null) {
                    y = exactParser.eval(xn, 0); //y=0 on this line just because it doesnt matter when evaluating the exact solution
                }
                error = (y - yn);
                relError = Math.abs(100 * (error / y));
            } catch(Exception e) {
                //most likely the user just didnt enter an expression for y
                yInputted = false;
            }
            if(yInputted) {
                resultBuilder.append(String.format("\n|%-3d|%-10f|%-10f|%-10f|%-10f|%-10f| |%-10f|%-10f|", n, xn, yn, y, Math.abs(error), relError, f, yn1));
            } else {
                resultBuilder.append(String.format("\n|%-3d|%-10f|%-10f| |%-10f|%-10f|", n, xn, yn, f, yn1));
            }
            points.add(new DoublePoint(xn, yn));
            if(yn > yMax) {
                yMax = yn;
            }
            if(yn < yMin) {
                yMin = yn;
            }
            xn += h;
            yn = yn1;
            n++;
        }
        if(!yInputted) {
            resultBuilder.replace(0, 84, "|n  |x_n       |y_n       | |f(x_n,y_n)|y_(n+1)   |");
        }
        gui.setOutputText(resultBuilder.toString());
        gui.setData(gui);
        gui.getGraphPanel().updateRegion(Double.parseDouble(gui.getX0Input()), xn-h, yMin, yMax);
        gui.getGraphPanel().updateDots(points);
        if(yInputted) {
            gui.getGraphPanel().updateYFunction(exactParser);
        } else {
            gui.getGraphPanel().updateYFunction(null);
        }
        gui.getGraphPanel().repaint(0, 0, 400, 400);
    }
}
