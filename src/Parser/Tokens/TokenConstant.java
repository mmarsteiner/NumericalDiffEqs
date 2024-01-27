package Parser.Tokens;

public class TokenConstant implements Token {
    private double value;

    public TokenConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return String.valueOf(value);
    }
}
