package symboltable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scanner.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 9th, 2017
 * <p>
 * Tests the SymbolTable class using JUnit5.
 */
class SymbolTableTest {

    private SymbolTable symbolT;

    /**
     * Create a symbol table and add functions, variables, arrays and programs to it before each test
     */
    @BeforeEach
    void setUp() {
        symbolT = new SymbolTable();

        //adding several function id's
        //ArrayList<Symbol> args = new ArrayList();
        //args.add(new Symbol("input", Kind.VARIABLE, Type.INTEGER));
        symbolT.addFunction("function1", Type.REAL);
        symbolT.addFunction("function2", Type.INTEGER);
        symbolT.addFunction("function3", Type.REAL);

        //adding variables
        symbolT.addVariable("var1", Type.INTEGER);
        symbolT.addVariable("var2", Type.REAL);
        symbolT.addVariable("var3", Type.INTEGER);

        //adding array id's
        symbolT.addArray("array1", Type.INTEGER, 0, 5);
        symbolT.addArray("array2", Type.REAL, -5, 5);
        symbolT.addArray("array3", Type.INTEGER, 10, 500);

        //adding program ids
        symbolT.addProgram("program1");
        symbolT.addProgram("program2");
        symbolT.addProgram("program3");

        //adding procedure ids
        symbolT.addProcedure("procedure1");
        symbolT.addProcedure("procedure2");
        symbolT.addProcedure("procedure3");
    }

    /**
     * Clear the symbol table and null the variables after each test.
     */
    @AfterEach
    void tearDown() {
        symbolT.symbTable.clear();
        symbolT.symbTable = null;
        symbolT = null;
    }

    /**
     * Tests adding two programs to the symbol table, one that does not already exist and one that does.
     */
    @Test
    void addProgram() {
        System.out.println("-----addProgram-----");

        // Add a program not already in the symbol table
        String name = "program0";
        boolean result = symbolT.addProgram(name);
        assertEquals(true, result);
        System.out.println("program0 successfully added to symbol table");

        // Add a program already in the symbol table
        name = "program1";
        result = symbolT.addProgram(name);
        assertEquals(false, result);
        System.out.println("program1 not added to symbol table, already exists\n");
    }

    /**
     * Tests adding two variables to the symbol table, one that does not already exist and one that does.
     */
    @Test
    void addVariable() {
        System.out.println("-----addVariable-----");

        // Add a variable not already in the symbol table
        String name = "var0";
        Type t = Type.INTEGER;
        boolean result = symbolT.addVariable(name, t);
        assertEquals(true, result);
        System.out.println("var0 successfully added to symbol table");

        // Add a variable already in the symbol table
        name = "var1";
        t = Type.REAL;
        result = symbolT.addVariable(name, t);
        assertEquals(false, result);
        System.out.println("var1 not added to symbol table, already exists\n");
    }

    /**
     * Tests adding two arrays to the symbol table, one that does not already exist and one that does.
     */
    @Test
    void addArray() {
        System.out.println("-----addArray-----");

        // Add a array not already in the symbol table
        String name = "array0";
        Type t = Type.INTEGER;
        int begin = -5, end = 0;
        boolean result = symbolT.addArray(name, t, begin, end);
        assertEquals(true, result);
        System.out.println("array0 successfully added to symbol table");

        // Add a array already in the symbol table
        name = "array1";
        t = Type.REAL;
        begin = -5;
        end = 0;
        result = symbolT.addArray(name, t, begin, end);
        assertEquals(false, result);
        System.out.println("array1 not added to symbol table, already exists\n");
    }

    /**
     * Tests adding two functions to the symbol table, one that does not already exist and one that does.
     */
    @Test
    void addFunction() {
        System.out.println("-----addFunction-----");

        // Add a function not already in the symbol table
        String name = "function0";
        Type t = Type.INTEGER;
        //ArrayList<Symbol> args = new ArrayList();
        //args.add(new Symbol("input", Kind.VARIABLE, Type.INTEGER));
        boolean result = symbolT.addFunction(name, t);
        assertEquals(true, result);
        System.out.println("function0 successfully added to symbol table");

        // Add a function already in the symbol table
        name = "function1";
        t = Type.REAL;
        result = symbolT.addFunction(name, t);
        assertEquals(false, result);
        System.out.println("function1 not added to symbol table, already exists\n");
    }

    /**
     * Tests adding two procedures to the symbol table, one that does not already exist and one that does.
     */
    @Test
    void addProcedure() {
        System.out.println("-----addProcedure-----");

        // Add a procedure not already in the symbol table
        String name = "procedure0";
        boolean result = symbolT.addProcedure(name);
        assertEquals(true, result);
        System.out.println("procedure0 successfully added to symbol table");

        // Add a procedure already in the symbol table
        name = "procedure1";
        result = symbolT.addProgram(name);
        assertEquals(false, result);
        System.out.println("procedure1 not added to symbol table, already exists\n");
    }

    /**
     * Tests method with a program symbol in the table, a non-program symbol in the table and a symbol not in the table.
     */
    @Test
    void isProgramName() {
        System.out.println("-----isProgramName-----");

        String name = "program1";
        boolean result = symbolT.isProgramName(name);
        assertEquals(true, result);
        System.out.println("program1 is a name in the symbol table and has kind PROGRAM");

        name = "var1";
        result = symbolT.isProgramName(name);
        assertEquals(false, result);
        System.out.println("var1 is a name in the symbol table but does not have kind PROGRAM");

        name = "foo";
        result = symbolT.isProgramName(name);
        assertEquals(false, result);
        System.out.println("foo is a not a name in the symbol table\n");
    }

    /**
     * Tests method with a variable symbol in the table, a non-variable symbol in the table and a symbol not in the table.
     */
    @Test
    void isVariableName() {
        System.out.println("-----isVariableName-----");

        String name = "var1";
        boolean result = symbolT.isVariableName(name);
        assertEquals(true, result);
        System.out.println("var1 is a name in the symbol table and has kind VARIABLE");

        name = "program1";
        result = symbolT.isVariableName(name);
        assertEquals(false, result);
        System.out.println("program1 is a name in the symbol table but does not have kind VARIABLE");

        name = "foo";
        result = symbolT.isVariableName(name);
        assertEquals(false, result);
        System.out.println("foo is a not a name in the symbol table\n");
    }

    /**
     * Tests method with an array symbol in the table, a non-array symbol in the table and a symbol not in the table.
     */
    @Test
    void isArrayName() {
        System.out.println("-----isArrayName-----");

        String name = "array1";
        boolean result = symbolT.isArrayName(name);
        assertEquals(true, result);
        System.out.println("array1 is a name in the symbol table and has kind ARRAY");

        name = "var1";
        result = symbolT.isArrayName(name);
        assertEquals(false, result);
        System.out.println("var1 is a name in the symbol table but does not have kind ARRAY");

        name = "foo";
        result = symbolT.isArrayName(name);
        assertEquals(false, result);
        System.out.println("foo is a not a name in the symbol table\n");
    }

    /**
     * Tests method with a function symbol in the table, a non-function symbol in the table and a symbol not in the table.
     */
    @Test
    void isFunctionName() {
        System.out.println("-----isFunctionName-----");

        String name = "function1";
        boolean result = symbolT.isFunctionName(name);
        assertEquals(true, result);
        System.out.println("function1 is a name in the symbol table and has kind FUNCTION");

        name = "var1";
        result = symbolT.isFunctionName(name);
        assertEquals(false, result);
        System.out.println("var1 is a name in the symbol table but does not have kind FUNCTION");

        name = "foo";
        result = symbolT.isFunctionName(name);
        assertEquals(false, result);
        System.out.println("foo is a not a name in the symbol table\n");
    }

    /**
     * Tests method with a procedure symbol in the table, a non-procedure symbol in the table and a symbol not in the table.
     */
    @Test
    void isProcedureName() {
        System.out.println("-----isProcedureName-----");

        String name = "procedure1";
        boolean result = symbolT.isProcedureName(name);
        assertEquals(true, result);
        System.out.println("procedure1 is a name in the symbol table and has kind PROGRAM");

        name = "var1";
        result = symbolT.isProcedureName(name);
        assertEquals(false, result);
        System.out.println("var1 is a name in the symbol table but does not have kind PROCEDURE");

        name = "foo";
        result = symbolT.isProcedureName(name);
        assertEquals(false, result);
        System.out.println("foo is a not a name in the symbol table\n");
    }

}