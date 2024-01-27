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
    public static void main(String[] args) {
        JFrame frame = new JFrame("Numerical Differential Equation Solver 1.0");
        NumericalSolverGUI gui = new NumericalSolverGUI();
        frame.setContentPane(gui.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
