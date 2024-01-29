package NumericalSolvers;

import Gui.DoublePoint;
import Gui.NumericalSolverGUI;
import Parser.EquationParser;

import java.util.ArrayList;

public class RK4Solver implements NumericalSolver {
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
        double yMin = yn;
        double yMax = yn;
        StringBuilder resultBuilder = new StringBuilder("|n  |x_n       |y_n       |y         |Error     |% Error   |");
        ArrayList<DoublePoint> points = new ArrayList<>();
        while(n <= iterations) {
            double k1 = dydxParser.eval(xn, yn);
            double k2 = dydxParser.eval(xn + (0.5*h), yn + (0.5*h*k1));
            double k3 = dydxParser.eval(xn + (0.5*h), yn + (0.5*h*k2));
            double k4 = dydxParser.eval(xn + h, yn + (h*k3));
            double yn1 = yn + ((h/6) * (k1 + 2*k2 + 2*k3 + k4));
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
            resultBuilder.append(String.format("\n|%-3d|%-10f|%-10f|%-10f|%-10f|%-10f|", n, xn, yn, y, error, relError));
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
        gui.setOutputText(resultBuilder.toString());
        gui.setData(gui);
        gui.getGraphPanel().updateRegion(Double.parseDouble(gui.getX0Input()), xn, yMin, yMax);
        gui.getGraphPanel().updateDots(points);
        if(yInputted) {
            gui.getGraphPanel().updateYFunction(exactParser);
        } else {
            gui.getGraphPanel().updateYFunction(null);
        }
        gui.getGraphPanel().repaint(0, 0, 400, 400);
    }
}
