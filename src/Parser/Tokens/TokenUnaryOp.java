package Parser.Tokens;

import java.util.function.Function;

public record TokenUnaryOp(String label, Function<Double, Double> eval) implements Token {

}
