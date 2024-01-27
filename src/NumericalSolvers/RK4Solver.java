package NumericalSolvers;

import Gui.NumericalSolverGUI;
import NumericalSolvers.NumericalSolver;
import Parser.EquationParser;

import java.util.function.BiFunction;

public class RK4Solver implements NumericalSolver {
    @Override
    public void solve(NumericalSolverGUI gui) {
        gui.getData(gui);
        EquationParser dydxParser = new EquationParser(gui.getDydxInput());
        EquationParser exactParser = null;
        try {
            exactParser = new EquationParser(gui.getExactSolutionInput());
        } catch (Exception e) {
            System.out.println("Not using exact solution because it failed to parse");
        }
        int n = 0;
        int iterations = Integer.parseInt(gui.getIterationsInput());
        double h = Double.parseDouble(gui.gethInput());
        double xn = Double.parseDouble(gui.getX0Input());
        double yn = Double.parseDouble(gui.getY0Input());
        double yn1 = 0;
        StringBuilder resultBuilder = new StringBuilder("|n  |x_n       |y_n       |y         |Error     |% Error   |");
        while(n <= iterations) {
            double k1 = dydxParser.eval(xn, yn);
            double k2 = dydxParser.eval(xn + (0.5*h), yn + (0.5*h*k1));
            double k3 = dydxParser.eval(xn + (0.5*h), yn + (0.5*h*k2));
            double k4 = dydxParser.eval(xn + h, yn + (h*k3));
            yn1 = yn + ((h/6) * (k1 + 2*k2 + 2*k3 + k4));
            double y = Double.NaN;
            double error = Double.NaN;
            double relError = Double.NaN;
            try {
                y = (exactParser == null) ? Double.NaN : exactParser.eval(xn, 0); //y doesnt matter here but this jawn built to process it and i aint changing it
                error = (exactParser == null) ? Double.NaN : (y - yn);
                relError = (exactParser == null) ? Double.NaN : Math.abs(100 * (error / y));
            } catch(Exception e) {
                //most likely the user just didnt enter an expression for y
            }
            resultBuilder.append(String.format("\n|%-3d|%-10f|%-10f|%-10f|%-10f|%-10f|", n, xn, yn, y, error, relError));
            xn += h;
            yn = yn1;
            n++;
        }
        gui.setOutputText(resultBuilder.toString());
        gui.setData(gui);
    }
}
