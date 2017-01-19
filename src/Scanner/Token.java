package Scanner;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * January 17th, 2017
 * <p>
 * This class contains the definition of a Token object. A token represents one lexeme of the file being parsed. Each
 * token has a lexeme which contains a string with the actual contents and a Type which is a value from the enum
 * class Type.
 */
public class Token {
    private String lexeme;
    private Type type;

    public Token(String input, Type t) {
        this.lexeme = input;
        this.type = t;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Type: " + this.type + ", Lexeme: " + this.lexeme;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Token.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Token other = (Token) obj;
        if (this.lexeme.equals(other.getLexeme()) && this.type.equals(other.getType()))
            return true;
        return false;
    }
}