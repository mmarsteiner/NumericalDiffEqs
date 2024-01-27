import Gui.NumericalSolverGUI;
import Parser.EquationParser;
import NumericalSolvers.EulerSolver;
import NumericalSolvers.ImpEulerSolver;
import NumericalSolvers.NumericalSolver;
import NumericalSolvers.RK4Solver;

import javax.swing.*;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Main {
    public enum MODE {
        EULER,IMP_EULER,RK4
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Numerical Differential Equation Solver 1.0");
        NumericalSolverGUI gui = new NumericalSolverGUI();
        frame.setContentPane(gui.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static NumericalSolver supplySolver(MODE mode) {
        return switch (mode) {
            case EULER -> new EulerSolver();
            case IMP_EULER -> new ImpEulerSolver();
            case RK4 -> new RK4Solver();
        };
    }
}
