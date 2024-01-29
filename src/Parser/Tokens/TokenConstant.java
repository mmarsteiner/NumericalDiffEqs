package Parser.Tokens;

public record TokenConstant(double value) implements Token {

    @Override
    public String label() {
        return String.valueOf(value);
    }
}
