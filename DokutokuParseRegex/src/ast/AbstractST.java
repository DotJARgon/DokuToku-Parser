package ast;

import lexer.Lexer;
import tokenizer.Tokenizer;
import utilities.DoubleLinked;

import java.util.ArrayList;

public class AbstractST {

    public static abstract class Token {
        public abstract Tokenizer.AST[] getTokens();
    }
    public static class Struct extends Token {
        private Tokenizer.AST access, name, contents;

        public Struct(Tokenizer.AST access, Tokenizer.AST name, Tokenizer.AST contents) {
            this.access = access;
            this.name = name;
            this.contents = contents;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("access: ")
                    .append(access)
                    .append(", ")
                    .append("struct name: ")
                    .append(name)
                    .append("contents: ")
                    .append(contents)
                    .toString();
        }

        @Override
        public Tokenizer.AST[] getTokens() {
            return new Tokenizer.AST[] {access, name, contents};
        }
    }

    public static void test(ArrayList<Tokenizer.AST> tokens) {
        DoubleLinked<Tokenizer.AST> list = new DoubleLinked<>(tokens);
        DoubleLinked.Node<Tokenizer.AST> start = list.getStart();

        ArrayList<Token> out = new ArrayList<>();

        while(start.hasNext()) {
            if(start.get().getToken().data.equals("struct")) {
                Tokenizer.AST access = start.prev().get();
                start = start.next();
                Tokenizer.AST name = start.get();
                start = start.next();
                Tokenizer.AST contents = start.get();

                out.add(new Struct(access, name, contents));
            }
            start = start.next();
        }

        for(Token token : out) {
            System.out.println(token);
        }
    }

}
