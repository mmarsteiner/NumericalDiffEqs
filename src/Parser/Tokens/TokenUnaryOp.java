package Parser.Tokens;

import java.util.function.Function;

public class TokenUnaryOp implements Token {
    private String label;
    private Function<Double, Double> eval;
    private boolean leftAssociative;

    public TokenUnaryOp(String label, Function<Double, Double> eval, boolean leftAssociative) {
        this.label = label;
        this.eval = eval;
        this.leftAssociative = leftAssociative;
    }

    public String getLabel() {
        return label;
    }

    public Function<Double, Double> getEval() {
        return eval;
    }

    public boolean isLeftAssociative() {
        return leftAssociative;
    }
}
