package symboltable;

import scanner.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 9th, 2017
 * <p>
 * This class creates a HashMap of different types of symbols we encounter in a pascal program. These symbols include
 * variables, programs, arrays and functions. Different information is stored depending on the type of symbol. Every
 * symbol stores a Type enum. Programs store the program name. Variables store the variable name and type (real or int).
 * Arrays store the name, type, begin and end index. Functions store the name, return type and an ArrayList of the
 * arguments.
 */
public class SymbolTable {

    // protected so it can be accessed for testing
    public HashMap<String, Symbol> symbTable;

    ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////

    /**
     * Creates a new symbol table and initializes the HashMap.
     */
    public SymbolTable() {
        symbTable = new HashMap<>();
    }

    ///////////////////////////////
    //       Constructors
    ///////////////////////////////

    /**
     * Returns a String representation of a SymbolTable
     *
     * @return a String representation of the object
     */
    @Override
    public String toString() {
        String retVal = "SymbolTable:\n";
        for (Map.Entry<String, Symbol> entry : symbTable.entrySet()) {
            retVal += entry.getKey() + " : " + entry.getValue();
        }
        return retVal;
    }

    ///////////////////////////////
    //       Methods
    ///////////////////////////////

    /**
     * Add a program symbol to the symbol table.
     *
     * @param name program name
     * @return true if added, false if already exists
     */
    public boolean addProgram(String name) {
        if (!symbTable.containsKey(name)) {
            symbTable.put(name, new Symbol(name, Kind.PROGRAM));
            return true;
        }
        return false;
    }

    /**
     * Add a variable symbol to the symbol table.
     *
     * @param name variable name
     * @param type variable type (real/int)
     * @return true if added, false if already exists
     */
    public boolean addVariable(String name, Type type) {
        if (!symbTable.containsKey(name)) {
            symbTable.put(name, new Symbol(name, Kind.VARIABLE, type));
            return true;
        }
        return false;
    }

    /**
     * Add an array symbol to the symbol table.
     *
     * @param name  array name
     * @param type  array type (real/int)
     * @param begin begin index of array
     * @param end   end index of array
     * @return true if added, false if already exists
     */
    public boolean addArray(String name, Type type, int begin, int end) {
        if (!symbTable.containsKey(name)) {
            symbTable.put(name, new Symbol(name, Kind.ARRAY, type, begin, end));
            return true;
        }
        return false;
    }

    /**
     * Add a function symbol to the symbol table.
     *
     * Note: removed args parameter, will re-add later
     *
     * @param name function name
     * @param type function return type
     * param args function arguments
     * @return true if added, false if already exists
     */
    public boolean addFunction(String name, Type type) {
        if (!symbTable.containsKey(name)) {
            symbTable.put(name, new Symbol(name, Kind.FUNCTION, type));
            return true;
        }
        return false;
    }

    /**
     * Add a procedure symbol to the symbol table.
     * <p>
     * Note: removed args parameter, will re-add later
     *
     * @param name procedure name
     *             param args procedure arguments
     * @return true if added, false if already exists
     */
    public boolean addProcedure(String name) {
        if (!symbTable.containsKey(name)) {
            symbTable.put(name, new Symbol(name, Kind.PROCEDURE));
            return true;
        }
        return false;
    }

    /**
     * Checks to see if a name exists in the symbol table and if it is a program symbol.
     *
     * @param name name of symbol to check
     * @return return true if symbol exists and is a program, false if not or does not exist
     */
    public boolean isProgramName(String name) {
        return symbTable.containsKey(name) && symbTable.get(name).getKind() == Kind.PROGRAM;
    }

    /**
     * Checks to see if a name exists in the symbol table and if it is a variable symbol.
     *
     * @param name name of symbol to check
     * @return return true if symbol exists and is a variable, false if not or does not exist
     */
    public boolean isVariableName(String name) {
        return symbTable.containsKey(name) && symbTable.get(name).getKind() == Kind.VARIABLE;
    }

    /**
     * Checks to see if a name exists in the symbol table and if it is an array symbol.
     *
     * @param name name of symbol to check
     * @return return true if symbol exists and is an array, false if not or does not exist
     */
    public boolean isArrayName(String name) {
        return symbTable.containsKey(name) && symbTable.get(name).getKind() == Kind.ARRAY;
    }

    /**
     * Checks to see if a name exists in the symbol table and if it is a function symbol.
     *
     * @param name name of symbol to check
     * @return return true if symbol exists and is a function, false if not or does not exist
     */
    public boolean isFunctionName(String name) {
        return symbTable.containsKey(name) && symbTable.get(name).getKind() == Kind.FUNCTION;
    }

    /**
     * Checks to see if a name exists in the symbol table and if it is a procedure symbol.
     *
     * @param name name of symbol to check
     * @return return true if symbol exists and is a function, false if not or does not exist
     */
    public boolean isProcedureName(String name) {
        return symbTable.containsKey(name) && symbTable.get(name).getKind() == Kind.PROCEDURE;
    }

    /**
     * Enum to track the different types of symbols that can be stored in the symbol table.
     */
    protected enum Kind {
        PROGRAM, VARIABLE, ARRAY, FUNCTION, PROCEDURE
    }

    /**
     * A data structure to store each symbol in the symbol table. A symbol always has a name and a kind. Depending on
     * the type of symbol different constructors will be called. The different constructors take in different
     * information and store it in a symbol object. Class is protected so it can be accessed for testing.
     */
    protected static class Symbol {

        ///////////////////////////////
        //    Instance Variables
        ///////////////////////////////

        private String id;
        private Type type;
        private Kind kind;
        private int beginidx, endidx;
        private ArrayList args;

        ///////////////////////////////
        //       Constructors
        ///////////////////////////////

        /**
         * Creates a Symbol to store a program symbol. Programs store the id name and the kind.
         *
         * @param i id of the program
         * @param k Kind enum (PROGRAM)
         */
        public Symbol(String i, Kind k) {
            id = i;
            kind = k;
        }

        /**
         * Creates a symbol to store a variable symbol. Variables store the variable id, kind and type (int/real).
         *
         * @param i id of the variable
         * @param k Kind enum (VARIABLE)
         * @param t type of the variable (int/real)
         */
        public Symbol(String i, Kind k, Type t) {
            id = i;
            kind = k;
            type = t;
        }

        /**
         * Creates a symbol to store an array symbol. Arrays store the array id, kind, begin index, end index and type
         * (int/real).
         *
         * @param i     id of the array
         * @param k     Kind enum (ARRAY)
         * @param t     type of the array (int/real)
         * @param begin beginning index
         * @param end   ending index
         */
        public Symbol(String i, Kind k, Type t, int begin, int end) {
            id = i;
            kind = k;
            type = t;
            beginidx = begin;
            endidx = end;
        }

        /**
         * Creates a symbol to store a function symbol. Functions store the function id, kind, return type (int/real)
         * and an ArrayList of the function arguments.
         *
         * @param i        id of the function
         * @param k        Kind enum (FUNCTION)
         * @param t        return type of the function (int/real)
         * @param funcArgs ArrayList of function arguments
         */
        public Symbol(String i, Kind k, Type t, ArrayList funcArgs) {
            id = i;
            kind = k;
            type = t;
            args = funcArgs;
        }

        /**
         * Creates a symbol to store a procedure symbol. Procedures store the procedure id, kind, and an ArrayList of
         * the function arguments.
         *
         * @param i        id of the procedure
         * @param k        Kind enum (PROCEDURE)
         * @param funcArgs ArrayList of function arguments
         */
        public Symbol(String i, Kind k, ArrayList funcArgs) {
            id = i;
            kind = k;
            args = funcArgs;
        }

        ///////////////////////////////
        //       Methods
        ///////////////////////////////

        /**
         * Gets the kind of the symbol
         *
         * @return A Kind enum, either PROGRAM, ARRAY, VARIABLE or FUNCTION
         */
        public Kind getKind() {
            return kind;
        }

        /**
         * Gets the id of the symbol
         *
         * @return A String with the symbol id
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the type of the symbol
         *
         * @return A Type enum of return type or variable type
         */
        public Type getType() {
            return type;
        }

        /**
         * Gets the beginning index of an array
         *
         * @return beginning index of an array
         */
        public int getBeginidx() {
            return beginidx;
        }

        /**
         * Gets the ending index of an array
         *
         * @return ending index of an array
         */
        public int getEndidx() {
            return endidx;
        }

        /**
         * Gets the arguments of a function
         *
         * @return An ArrayList of function arguments
         */
        public ArrayList getArgs() {
            return args;
        }

        /**
         * @return a string version of the object
         */
        @Override
        public String toString() {
            return "id='" + id + '\'' + ", kind=" + kind + ", type=" + type + ", beginidx=" + beginidx + ", endidx=" + endidx + "\n";
        }

        /**
         * Checks to see if two Symbols are equal to each other.
         *
         * @param o A Symbol object to compare
         * @return true if equal, false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Symbol symbol = (Symbol) o;
            return getBeginidx() == symbol.getBeginidx() && getEndidx() == symbol.getEndidx() && Objects.equals(getId(), symbol.getId()) && Objects.equals(getType(), symbol.getType()) && getKind() == symbol.getKind() && Objects.equals(getArgs(), symbol.getArgs());
        }

    }

}