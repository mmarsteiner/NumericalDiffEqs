package Parser.Tokens;

import java.util.function.BiFunction;

public class TokenBinaryOp implements Token {
    private String label;
    private BiFunction<Double, Double, Double> eval;

    public TokenBinaryOp(String label, BiFunction<Double, Double, Double> eval) {
        this.label = label;
        this.eval = eval;
    }

    public String getLabel() {
        return label;
    }

    public BiFunction<Double, Double, Double> getEval() {
        return eval;
    }
}
