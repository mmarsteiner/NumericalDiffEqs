package Parser.Tokens;

public record TokenVariable(char varName) implements Token {

    @Override
    public String label() {
        return String.valueOf(varName);
    }
}
