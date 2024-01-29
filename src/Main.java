import Gui.NumericalSolverGUI;
import javax.swing.*;

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
