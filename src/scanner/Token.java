package scanner;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * January 17th, 2017
 * <p>
 * This class contains the definition of a Token object. A token represents one lexeme of the file being parsed. Each
 * token has a lexeme which contains a string with the actual contents and a Type which is a value from the enum
 * class Type.
 *
 * @author Bob Laskowski
 */
public class Token {
    private String lexeme;
    private Type type;

    public Token(String input, Type t) {
        this.lexeme = input;
        this.type = t;
    }

    /**
     * Returns the String the Token holds
     *
     * @return The String the Token holds
     */
    public String getLexeme() {
        return this.lexeme;
    }

    /**
     * Gets the Type the Token holds from the Type Enum
     *
     * @return The Type of Toekn
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Formats the output of a Token: Type: xxxx, Lexeme: xxxx
     *
     * @return The Type and lexeme of a Token
     */
    @Override
    public String toString() {
        return "Type: " + this.type + ", Lexeme: " + this.lexeme;
    }

    /**
     * Two Tokens are equal if they have the same lexeme and Type
     *
     * @param obj Another Token object
     * @return True if they are equal, False otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Token.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Token other = (Token) obj;
        if (this.lexeme.equals(other.getLexeme()) && this.type.equals(other.getType())) return true;
        return false;
    }
}