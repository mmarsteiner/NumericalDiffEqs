package Parser.Tokens;

import java.util.function.BiFunction;

public record TokenBinaryOp(String label, BiFunction<Double, Double, Double> eval) implements Token {
}
