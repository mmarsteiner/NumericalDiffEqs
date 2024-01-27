package Parser.Tokens;

public class TokenVariable implements Token {
    private char varName;

    public TokenVariable(char name) {
        this.varName = name;
    }

    public char getVarName() {
        return varName;
    }

    @Override
    public String getLabel() {
        return String.valueOf(varName);
    }
}
