package lexer;

import ast.AbstractST;
import tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static enum Type {
        CONTAINER("[{|}|\\(|\\)|\\[|\\]|\\<|\\>|\"|'|]"),
        DECIMAL("-?[0-9]+\\.[0-9]+"),
        NUMBER("-?[0-9]+"),
        BINARYOP("[*|/|+|-]"),
        ALPHANUMERIC("[a-zA-Z]+[a-zA-Z0-9_]*"),
        WHITESPACE("[ \t\f\r\n]+"),
        UNRECOGNIZED(".?")
        ;
        public final String pattern;

        private Type(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {
        public Type type;
        public String data;

        public Token(Type type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("%s:%s", type.name(), data);
        }
    }

    public static ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();

        StringBuffer tokenPatternBuffer = new StringBuffer();

        for(Type type : Type.values()) {
            tokenPatternBuffer.append(String.format("|(?<%s>%s)", type.name(), type.pattern));
        }

        Pattern tokenPatterns = Pattern.compile(tokenPatternBuffer.substring(1));

        Matcher matcher = tokenPatterns.matcher(input);

        while(matcher.find()) {
            for(Type type : Type.values()) {
                if(matcher.group(type.name()) != null) {
                    tokens.add(new Token(type, matcher.group(type.name())));
                }
            }
        }

        return tokens;
    }

    public static void main(String args[]) {
        String test = "accessibility<@hello_friend> struct Foo {}";
//        ArrayList<Token> tokens = Lexer.lex(test);
//        for(Token token : tokens) {
//            System.out.println(token);
//        }
//
//        System.out.println(Tokenizer.tokenize(test));
        AbstractST.test(Tokenizer.tokenize(test).getContents());
    }
}
