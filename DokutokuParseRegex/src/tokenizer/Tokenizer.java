package tokenizer;

import lexer.Lexer;

import java.util.ArrayList;
import java.util.Iterator;

public class Tokenizer {

    private static final String open  = "{([<\"'";
    private static final String close = "})]>\"'";

    public static class AST {
        protected Lexer.Token token;
        public AST(Lexer.Token token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return token.toString();
        }

        public Lexer.Token getToken() {
            return token;
        }
    }

    public static class Container extends AST {
        private ArrayList<AST> ast;
        public Container(Lexer.Token token, ArrayList<AST> ast) {
            super(token);
            this.ast = ast;
        }

        public ArrayList<AST> getContents() {
            return ast;
        }

        @Override
        public String toString() {
            
            StringBuilder sb = new StringBuilder();
            sb.append(token.data);

            String contents = ast.toString();
            sb.append(contents.substring(1, contents.length() - 1));

            sb.append(close.charAt(open.indexOf(token.data)));
            return sb.toString();
        }
    }

    public static Container tokenize(String input) {
        return new Container(new Lexer.Token(Lexer.Type.CONTAINER, "{"), tokenize(Lexer.lex(input)));
    }

    private static ArrayList<AST> tokenize(ArrayList<Lexer.Token> tokens) {
        ArrayList<AST> ast = new ArrayList<>();

        Iterator<Lexer.Token> tokenIt = tokens.iterator();
        Lexer.Token token;

        while(tokenIt.hasNext()) {
            token = tokenIt.next();
            if(token.type != Lexer.Type.CONTAINER) {
                ast.add(new AST(token));
            }
            else if(token.data.length() == 1 && open.contains(token.data)) {
                char o = token.data.charAt(0);
                char c = close.charAt(open.indexOf(o));

                ArrayList<AST> inner = new ArrayList<>();

                int count = 1;

                Lexer.Token in;

                while(count > 0 && tokenIt.hasNext()) {
                    in = tokenIt.next();

                    if(in.type == Lexer.Type.CONTAINER && in.data.length() == 1 && in.data.charAt(0) == c) {
                        if(count != 1) {
                            inner.add(new AST(in));
                        }
                        count--;
                    }
                    else {
                        inner.add(new AST(in));
                    }
                }

                ast.add(new Container(token, inner));
            }
        }

        return ast;
    }

}
