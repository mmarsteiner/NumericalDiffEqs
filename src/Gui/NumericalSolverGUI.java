package Gui;

import NumericalSolvers.EulerSolver;
import NumericalSolvers.ImpEulerSolver;
import NumericalSolvers.RK4Solver;

import javax.swing.*;

public class NumericalSolverGUI {
    private JTextField dydxInputField;
    private JRadioButton eulersMethodRadioButton;
    private JRadioButton improvedEulersMethodRadioButton;
    private JRadioButton rk4MethodRadioButton;
    private JTextField hInputField;
    private JTextField iterationsInputField;
    private JTextField exactSolutionInputField;
    private JButton calculateButton;
    private JPanel contentPane;
    private JTextArea outputTextArea;
    private JTextField x0InputField;
    private JTextField y0InputField;
    private GraphPanel graphPanel;
    private ButtonGroup methodSelection;
    private String dydxInput;
    private String hInput;
    private String iterationsInput;
    private String exactSolutionInput;
    private String outputText;
    private String x0Input;
    private String y0Input;

    public NumericalSolverGUI() {
        calculateButton.addActionListener(actionEvent -> {
            if(eulersMethodRadioButton.isSelected()) {
                solveWithEuler();
            } else if(improvedEulersMethodRadioButton.isSelected()) {
                solveWithImpEuler();
            } else { //rk4 chosen
                solveWithRK4();
            }
        });
    }

    private void solveWithEuler() {
        new EulerSolver().solve(this);
    }

    private void solveWithImpEuler() {
        new ImpEulerSolver().solve(this);
    }

    private void solveWithRK4() {
        new RK4Solver().solve(this);
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public String getDydxInput() {
        return dydxInput;
    }

    public void setDydxInput(final String dydxInput) {
        this.dydxInput = dydxInput;
    }

    public String getHInput() {
        return hInput;
    }

    public void sethInput(final String hInput) {
        this.hInput = hInput;
    }

    public String getIterationsInput() {
        return iterationsInput;
    }

    public void setIterationsInput(final String iterationsInput) {
        this.iterationsInput = iterationsInput;
    }

    public String getExactSolutionInput() {
        return exactSolutionInput;
    }

    public void setExactSolutionInput(final String exactSolutionInput) {
        this.exactSolutionInput = exactSolutionInput;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(final String outputText) {
        this.outputText = outputText;
    }

    public String getX0Input() {
        return x0Input;
    }

    public void setX0Input(final String x0Input) {
        this.x0Input = x0Input;
    }

    public String getY0Input() {
        return y0Input;
    }

    public void setY0Input(final String y0Input) {
        this.y0Input = y0Input;
    }

    public void setData(NumericalSolverGUI data) {
        dydxInputField.setText(data.getDydxInput());
        hInputField.setText(data.getHInput());
        x0InputField.setText(data.getX0Input());
        y0InputField.setText(data.getY0Input());
        iterationsInputField.setText(data.getIterationsInput());
        exactSolutionInputField.setText(data.getExactSolutionInput());
        outputTextArea.setText(data.getOutputText());
    }

    public void getData(NumericalSolverGUI data) {
        data.setDydxInput(dydxInputField.getText());
        data.sethInput(hInputField.getText());
        data.setX0Input(x0InputField.getText());
        data.setY0Input(y0InputField.getText());
        data.setIterationsInput(iterationsInputField.getText());
        data.setExactSolutionInput(exactSolutionInputField.getText());
        data.setOutputText(outputTextArea.getText());
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    private void createUIComponents() {
        graphPanel = new GraphPanel();
    }
}
