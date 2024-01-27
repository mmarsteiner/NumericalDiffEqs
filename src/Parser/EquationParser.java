package Parser;

import Parser.Tokens.*;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Stack;

public class EquationParser {
    private String expr;
    private ArrayDeque<Token> eqnStack;
    public EquationParser(String expr) {
        this.expr = expr;
        ArrayDeque<Token> rawTokens = readDataToStack(expr);
        eqnStack = createEvalStack(rawTokens);
    }

    public double eval(double x, double y) {
        HashMap<Character, Double> xy = new HashMap<>();
        xy.put('x', x);
        xy.put('y', y);
        return evalEquationStack(eqnStack, xy);
    }

    private static double evalEquationStack(ArrayDeque<Token> equation, HashMap<Character, Double> variableValues) {
        Stack<Double> operandStack = new Stack<>();
        ArrayDeque<Token> eqn = equation.clone();
        while(!eqn.isEmpty()) {
            Token t = eqn.removeLast();
            if(t instanceof TokenConstant) {
                operandStack.push(((TokenConstant) t).getValue());
            } else if(t instanceof TokenVariable) {
                char label = ((TokenVariable) t).getVarName();
                if(!variableValues.containsKey(label)) throw new IllegalArgumentException("Value for variable '" + label + "' not supplied");
                operandStack.push(variableValues.get(label));
            } else if(t instanceof TokenUnaryOp) {
                if(operandStack.isEmpty()) throw new IllegalStateException("Unary op has no available operands");
                operandStack.push(((TokenUnaryOp) t).getEval().apply(operandStack.pop()));
            } else if(t instanceof TokenBinaryOp) {
                if(operandStack.size() < 2) throw new IllegalStateException("Binary op has too few available operands");
                double val1 = operandStack.pop();
                double val2 = operandStack.pop();
                operandStack.push(((TokenBinaryOp) t).getEval().apply(val2, val1));
            } else {
                throw new IllegalStateException("Invalid postfix notation");
            }
        }
        if(operandStack.size() != 1) {
            throw new IllegalStateException("Invalid postfix notation did not reduce correctly");
        }
        return operandStack.pop();
    }

    //shunting yard algorithm
    private static ArrayDeque<Token> createEvalStack(ArrayDeque<Token> input) {
        ArrayDeque<Token> inputStack = input.clone();
        ArrayDeque<Token> evalStack = new ArrayDeque<>();
        ArrayDeque<Token> opStack = new ArrayDeque<>();

        while(!inputStack.isEmpty()) {
            Token t = inputStack.peekLast();
            if(t instanceof TokenConstant || t instanceof TokenVariable) {
                evalStack.addFirst(t);
            } else if(t instanceof TokenUnaryOp) {
                opStack.addFirst(t);
            } else if(t instanceof TokenBinaryOp) {
                while(!opStack.isEmpty() &&
                        !(opStack.peekFirst() instanceof TokenLeftParen) &&
                        getOpPrecedence(opStack.peekFirst().getLabel()) >= getOpPrecedence(t.getLabel())) {
                    evalStack.addFirst(opStack.removeFirst());
                }
                opStack.push(t);
            } else if(t instanceof TokenLeftParen) {
                opStack.push(t);
            } else if(t instanceof TokenRightParen) {
                while(!opStack.isEmpty() && !(opStack.peekFirst() instanceof TokenLeftParen)) {
                    evalStack.push(opStack.removeFirst());
                }
                if(!opStack.isEmpty() && opStack.peekFirst() instanceof TokenLeftParen) {
                    opStack.removeFirst();
                    if(!opStack.isEmpty() && opStack.peekFirst() instanceof TokenUnaryOp) {
                        evalStack.addFirst(opStack.removeFirst());
                    }
                }
            }
            inputStack.removeLast();
        }
        while(!opStack.isEmpty()) {
            evalStack.addFirst(opStack.removeFirst());
        }
        return evalStack;
    }

    private static int getOpPrecedence(String label) {
        return switch(label) {
            case "^", "root" -> 4;
            case "*", "/", "_" -> 3;
            case "+", "-" -> 2;
            default -> throw new IllegalStateException("Unexpected binary op label: " + label);
        };
    }

    private static ArrayDeque<Token> readDataToStack(String in) {
        ArrayDeque<Token> res = new ArrayDeque<>();
        int idx = 0;
        while(idx < in.length()) {
            Token t;
            char currChar = in.charAt(idx);
            String twoChars = (idx < in.length() - 1) ? in.substring(idx, idx+2) : null;
            String threeChars = (idx < in.length() - 2) ? in.substring(idx, idx+3) : null;
            String fourChars = (idx < in.length() - 3) ? in.substring(idx, idx+4) : null;
            String sixChars = (idx < in.length() - 5) ? in.substring(idx, idx+6) : null;
            //variable
            switch (currChar) {
                case 'X', 'x' -> {
                    t = new TokenVariable('x');
                    res.addFirst(t);
                    idx++;
                    continue;
                }
                case 'Y', 'y' -> {
                    t = new TokenVariable('y');
                    res.addFirst(t);
                    idx++;
                    continue;
                }
            }
            //pi and e
            if(currChar == 'e') {
                t = new TokenConstant(Math.E);
                res.addFirst(t);
                idx++;
                continue;
            } else if(twoChars != null && twoChars.equalsIgnoreCase("pi")) {
                t = new TokenConstant(Math.PI);
                res.addFirst(t);
                idx += 2;
                continue;
            }
            //number
            if('0' <= currChar && currChar <= '9') {
                double val = 0;
                while(idx < in.length() && '0' <= in.charAt(idx) && in.charAt(idx) <= '9') {
                    val *= 10;
                    val += in.charAt(idx) - '0';
                    idx++;
                }
                if(idx < in.length() && in.charAt(idx) != '.') {
                    t = new TokenConstant(val);
                    res.addFirst(t);
                    continue;
                }
                idx++;
                double multiplier = 0.1;
                while(idx < in.length() && '0' <= in.charAt(idx) && in.charAt(idx) <= '9') {
                    val += (multiplier * (in.charAt(idx) - '0'));
                    multiplier /= 10;
                    idx++;
                }
                t = new TokenConstant(val);
                res.addFirst(t);
                continue;
            }
            //unary ops
            //negation is '_' to avoid confusion with subtraction
            if(currChar == '_') {
                t = new TokenUnaryOp("_", (x) -> (-1 * x), true);
                res.addFirst(t);
                idx++;
                continue;
            } else if(fourChars != null && fourChars.equalsIgnoreCase("sqrt")) {
                t = new TokenUnaryOp("sqrt", Math::sqrt, true);
                res.addFirst(t);
                idx += 4;
                continue;
            } else if(fourChars != null && fourChars.equalsIgnoreCase("cbrt")) {
                t = new TokenUnaryOp("cbrt", Math::cbrt, true);
                res.addFirst(t);
                idx += 4;
                continue;
            } else if(twoChars != null && twoChars.equalsIgnoreCase("ln")) {
                t = new TokenUnaryOp("ln", Math::log, true);
                res.addFirst(t);
                idx += 2;
                continue;
            } else if(threeChars != null && threeChars.equalsIgnoreCase("exp")) {
                t = new TokenUnaryOp("exp", Math::exp, true);
                res.addFirst(t);
                idx += 3;
                continue;
            } else if(threeChars != null && threeChars.equalsIgnoreCase("log")) {
                t = new TokenUnaryOp("log", Math::log10, true);
                res.addFirst(t);
                idx += 3;
                continue;
            } else if(threeChars != null && threeChars.equalsIgnoreCase("sin")) {
                t = new TokenUnaryOp("sin", Math::sin, true);
                res.addFirst(t);
                idx += 3;
                continue;
            } else if(threeChars != null && threeChars.equalsIgnoreCase("cos")) {
                t = new TokenUnaryOp("cos", Math::cos, true);
                res.addFirst(t);
                idx += 3;
                continue;
            } else if(threeChars != null && threeChars.equalsIgnoreCase("tan")) {
                t = new TokenUnaryOp("tan", Math::tan, true);
                res.addFirst(t);
                idx += 3;
                continue;
            } else if(sixChars != null && sixChars.equalsIgnoreCase("arcsin")) {
                t = new TokenUnaryOp("arcsin", Math::asin, true);
                res.addFirst(t);
                idx += 6;
                continue;
            } else if(sixChars != null && sixChars.equalsIgnoreCase("arccos")) {
                t = new TokenUnaryOp("arccos", Math::acos, true);
                res.addFirst(t);
                idx += 6;
                continue;
            } else if(sixChars != null && sixChars.equalsIgnoreCase("arctan")) {
                t = new TokenUnaryOp("arctan", Math::atan, true);
                res.addFirst(t);
                idx += 6;
                continue;
            }
            //binary ops
            switch (currChar) {
                case '+' -> {
                    t = new TokenBinaryOp("+", Double::sum);
                    res.addFirst(t);
                    idx++;
                    continue;
                }
                case '-' -> {
                    t = new TokenBinaryOp("-", (x, y) -> (x - y));
                    res.addFirst(t);
                    idx++;
                    continue;
                }
                case '*' -> {
                    t = new TokenBinaryOp("*", (x, y) -> (x * y));
                    res.addFirst(t);
                    idx++;
                    continue;
                }
                case '/' -> {
                    t = new TokenBinaryOp("/", (x, y) -> (x / y));
                    res.addFirst(t);
                    idx++;
                    continue;
                }
                case '^' -> {
                    t = new TokenBinaryOp("^", Math::pow);
                    res.addFirst(t);
                    idx++;
                    continue;
                }
            }
            //parentheses
            if(currChar == '(') {
                t = new TokenLeftParen();
                res.addFirst(t);
                idx++;
                continue;
            } else if(currChar == ')') {
                t = new TokenRightParen();
                res.addFirst(t);
                idx++;
                continue;
            }
            System.out.println("Invalid token '" + currChar + "', ignoring it");
            idx++;
        }
        return res;
    }


}
