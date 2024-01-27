package NumericalSolvers;

import Gui.NumericalSolverGUI;
import NumericalSolvers.NumericalSolver;
import Parser.EquationParser;

import java.util.function.BiFunction;

public class ImpEulerSolver implements NumericalSolver {
    @Override
    public void solve(NumericalSolverGUI gui) {
        gui.getData(gui);
        EquationParser dydxParser = new EquationParser(gui.getDydxInput());
        EquationParser exactParser = null;
        try {
            exactParser = new EquationParser(gui.getExactSolutionInput());
        } catch (Exception e) {
            System.out.println("Not using exact solution");
        }
        int n = 0;
        int iterations = Integer.parseInt(gui.getIterationsInput());
        double h = Double.parseDouble(gui.gethInput());
        double xn = Double.parseDouble(gui.getX0Input());
        double yn = Double.parseDouble(gui.getY0Input());
        double yn1 = 0;
        StringBuilder resultBuilder = new StringBuilder("|n  |x_n       |y_n       |y         |Error     |% Error   |");
        while(n <= iterations) {
            double f = dydxParser.eval(xn, yn);
            double yn1star = yn + (h * f);
            double fimp = dydxParser.eval(xn + h, yn1star);
            yn1 = yn + ((h / 2) * (f + fimp));
            double y = Double.NaN;
            double error = Double.NaN;
            double relError = Double.NaN;
            try {
                y = exactParser.eval(xn, 0); //y doesnt matter here but this jawn built to process it and i aint changing it
                error = y - yn;
                relError = Math.abs(100 * (error / y));
            } catch (Exception e) {
                //System.out.println("couldnt process optional y expression");
            }

            resultBuilder.append(String.format("\n|%-3d|%-10f|%-10f|%-10f|%-10f|%-10f|", n, xn, yn, y, error, relError));
            xn += h;
            yn = yn1;
            n++;
        }
        gui.setOutputText(resultBuilder.toString());
        gui.setData(gui);
    }

    //old, used for command line version
    /*@Override
    public void solve(BiFunction<Double, Double, Double> dydx, double x0, double y0, double h, int iterations) {
        System.out.println("Solving with improved Euler's method, x0=" + x0 + ", h=" + h + ", " + iterations + " iterations");
        System.out.println("|n  |x_n        |y_n        |f(x_n,y_n) |y*_(n+1)   |f(xn1,yn1*)|y_(n+1)    |");
        int n = 0;
        double xn = x0;
        double yn = y0;
        double yn1star = 0;
        double yn1 = 0;
        while(n <= iterations) {
            double f = dydx.apply(xn,yn);
            yn1star = yn + (h * f);
            double fimp = dydx.apply(xn + h, yn1star);
            yn1 = yn + ((h / 2) * (f + fimp));
            System.out.printf("|%-3d|%-11f|%-11f|%-11f|%-11f|%-11f|%-11f|\n", n, xn, yn, f, yn1star, fimp, yn1);
            xn += h;
            yn = yn1;
            n++;
        }
    }*/
}
