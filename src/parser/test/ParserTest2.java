package parser.test;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * March 14th, 2017
 * <p>
 * This class uses the JUnit framework to test the Parser class now that it creates a syntax tree. For testing purposes
 * the input file "simple.pas" is used to test parsing a whole program as well as smaller Strings to test specific
 * aspects of the parser.
 *
 * @author Bob Laskowski
 */
class ParserTest2 {
/*
    @Test
    void factor() {
        // Just a value
        Parser parser = new Parser("4");
        ExpressionNode actual = parser.factor();
        String actualString = actual.indentedToString(0);
        String expectedString = "Value: 4, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 1 passed.");

        // Just an id
        parser = new Parser("foo");
        parser.getSymbolTable().addVariable("foo", Type.INTEGER);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Name: foo, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 2 passed.");

        // Array id
        parser = new Parser("foo[4*5]");
        parser.getSymbolTable().addArray("foo", Type.INTEGER, 0, 100);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Array: foo, Type: INTEGER\n" + "|-- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "|-- --- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 3 passed.");

        // Procedure/function call with expression argument
        parser = new Parser("foo(4*5)");
        parser.getSymbolTable().addProcedure("foo");
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Name: foo\n" + "Arguments: \n" + "|-- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "|-- --- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 4 passed.");

        // Procedure/function call with multiple arguments
        parser = new Parser("foo(4, fum, 4*5)");
        parser.getSymbolTable().addFunction("foo", Type.INTEGER);
        parser.getSymbolTable().addVariable("fum", Type.INTEGER);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Name: foo\n" + "Arguments: \n" + "|-- Value: 4, Type: INTEGER\n" + "|-- Name: fum, Type: INTEGER\n" + "|-- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "|-- --- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 5 passed.");

        //expression in paren
        parser = new Parser("(4*5)");
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Operation: ASTERISK, Type: INTEGER\n" + "|-- Value: 4, Type: INTEGER\n" + "|-- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 6 passed.");

        // not factor
        parser = new Parser("not 4");
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Unary Operation: NOT, Type: INTEGER\n" + "|-- Value: 4, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 7 passed.");
    }

    @Test
    void simple_expression() {
        Parser parser = new Parser("foo * 8 + bar");
        parser.getSymbolTable().addVariable("foo", Type.INTEGER);
        parser.getSymbolTable().addVariable("bar", Type.REAL);
        ExpressionNode actual = parser.simple_expression();
        String actualString = actual.indentedToString(0);
        String expectedString = "Operation: PLUS, Type: REAL\n" + "|-- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- --- Value: 8, Type: INTEGER\n" + "|-- Name: bar, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 1 passed.");

        parser = new Parser("-4 + foo[8] * (3 + 5)");
        parser.getSymbolTable().addArray("foo", Type.INTEGER, 1, 10);
        actual = parser.simple_expression();
        actualString = actual.indentedToString(0);
        expectedString = "Unary Operation: MINUS, Type: INTEGER\n" + "|-- Operation: PLUS, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "|-- --- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- --- Array: foo, Type: INTEGER\n" + "|-- --- --- --- Value: 8, Type: INTEGER\n" + "|-- --- --- Operation: PLUS, Type: INTEGER\n" + "|-- --- --- --- Value: 3, Type: INTEGER\n" + "|-- --- --- --- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 2 passed.");

        parser = new Parser("not foo - 8 / bar");
        parser.getSymbolTable().addVariable("foo", Type.INTEGER);
        parser.getSymbolTable().addVariable("bar", Type.REAL);
        actual = parser.simple_expression();
        actualString = actual.indentedToString(0);
        expectedString = "Operation: MINUS, Type: REAL\n" + "|-- Unary Operation: NOT, Type: INTEGER\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- Operation: FSLASH, Type: REAL\n" + "|-- --- Value: 8, Type: INTEGER\n" + "|-- --- Name: bar, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 3 passed.");
    }

    // Read/write??
    @Test
    void statement() {
        Parser parser = new Parser("fub := foo * 8 + bar < not foo - 8 / bar");
        SymbolTable tempTable = parser.getSymbolTable();
        tempTable.addVariable("foo", Type.INTEGER);
        tempTable.addVariable("bar", Type.INTEGER);
        tempTable.addVariable("fub", Type.INTEGER);
        StatementNode actual = parser.statement();
        String actualString = actual.indentedToString(0);
        String expectedString = "Assignment:\n" + "|-- Name: fub, Type: INTEGER\n" + "|-- Operation: LTHAN, Type: INTEGER\n" + "|-- --- Operation: PLUS, Type: INTEGER\n" + "|-- --- --- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- --- --- Name: foo, Type: INTEGER\n" + "|-- --- --- --- Value: 8, Type: INTEGER\n" + "|-- --- --- Name: bar, Type: INTEGER\n" + "|-- --- Operation: MINUS, Type: INTEGER\n" + "|-- --- --- Unary Operation: NOT, Type: INTEGER\n" + "|-- --- --- --- Name: foo, Type: INTEGER\n" + "|-- --- --- Operation: FSLASH, Type: INTEGER\n" + "|-- --- --- --- Value: 8, Type: INTEGER\n" + "|-- --- --- --- Name: bar, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("statement test 1 passed.");

        parser = new Parser("if foo < 4 then fub := 5 else foo := 5)");
        tempTable = parser.getSymbolTable();
        tempTable.addVariable("foo", Type.INTEGER);
        tempTable.addVariable("fub", Type.REAL);
        actual = parser.statement();
        actualString = actual.indentedToString(0);
        expectedString = "If:\n" + "|-- Operation: LTHAN, Type: INTEGER\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "Then:\n" + "|-- Assignment:\n" + "|-- --- Name: fub, Type: REAL\n" + "|-- --- Value: 5, Type: REAL\n" + "Else:\n" + "|-- Assignment:\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- --- Value: 5, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("statement test 2 passed.");

        parser = new Parser("while foo = 5 do if fub < 4 then fub := 6 else fub := 4");
        tempTable = parser.getSymbolTable();
        tempTable.addVariable("foo", Type.INTEGER);
        tempTable.addVariable("fub", Type.REAL);
        actual = parser.statement();
        actualString = actual.indentedToString(0);
        expectedString = "While:\n" + "|-- Operation: EQUAL, Type: INTEGER\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- --- Value: 5, Type: INTEGER\n" + "Do:\n" + "|-- If:\n" + "|-- --- Operation: LTHAN, Type: REAL\n" + "|-- --- --- Name: fub, Type: REAL\n" + "|-- --- --- Value: 4, Type: INTEGER\n" + "|-- Then:\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fub, Type: REAL\n" + "|-- --- --- Value: 6, Type: REAL\n" + "|-- Else:\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fub, Type: REAL\n" + "|-- --- --- Value: 4, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("statement test 3 passed.");

        // Test procedure_statement
        parser = new Parser("calculate(1, 2, foo)");
        tempTable = parser.getSymbolTable();
        tempTable.addProcedure("calculate");
        tempTable.addVariable("foo", Type.INTEGER);
        actual = parser.statement();
        actualString = actual.indentedToString(0);
        expectedString = "Procedure: calculate\n" + "|-- Value: 1, Type: INTEGER\n" + "|-- Value: 2, Type: INTEGER\n" + "|-- Name: foo, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("statement test 4 passed.");

        // Test compound_statement
        parser = new Parser("begin foo := 4; calculate(1,2,3) end");
        parser.getSymbolTable().addVariable("foo", Type.INTEGER);
        parser.getSymbolTable().addProcedure("calculate");
        actual = parser.statement();
        actualString = actual.indentedToString(0);
        expectedString = "Compound Statement\n" + "|-- Assignment:\n" + "|-- --- Name: foo, Type: INTEGER\n" + "|-- --- Value: 4, Type: INTEGER\n" + "|-- Procedure: calculate\n" + "|-- --- Value: 1, Type: INTEGER\n" + "|-- --- Value: 2, Type: INTEGER\n" + "|-- --- Value: 3, Type: INTEGER\n";

        assertEquals(expectedString, actualString);
        System.out.println("statement test 5 passed.");
    }

    @Test
    void subprogram_declaration() {
        Parser parser = new Parser("function calculate(foo, bar : integer ) : integer ; begin end");
        SubProgramNode actual = parser.subprogram_declaration();
        String actualString = actual.indentedToString(0);
        String expectedString = "SubProgram: calculate, Return: INTEGER\n" + "Arguments: (foo [INTEGER], bar [INTEGER])\n" + "|-- Declarations\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n";

        assertEquals(expectedString, actualString);
        System.out.println("subprogram_declaration test 1 passed.");

        parser = new Parser("function calculate(foo, bar : real ) : real ; var fub, fum : real ; begin fub := foo + bar ; fum := bar - foo end");
        actual = parser.subprogram_declaration();
        actualString = actual.indentedToString(0);
        expectedString = "SubProgram: calculate, Return: REAL\n" + "Arguments: (foo [REAL], bar [REAL])\n" + "|-- Declarations\n" + "|-- --- Name: fub, Type: REAL\n" + "|-- --- Name: fum, Type: REAL\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fub, Type: REAL\n" + "|-- --- --- Operation: PLUS, Type: REAL\n" + "|-- --- --- --- Name: foo, Type: REAL\n" + "|-- --- --- --- Name: bar, Type: REAL\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fum, Type: REAL\n" + "|-- --- --- Operation: MINUS, Type: REAL\n" + "|-- --- --- --- Name: bar, Type: REAL\n" + "|-- --- --- --- Name: foo, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("subprogram_declaration test 2 passed.");

        parser = new Parser("function calculate(foo, bar : real ) : real ; var fub, fum : real ; procedure dell(fi, croix : real) ; var soft: integer ; begin fi := fi + croix end; begin fub := foo + bar ; fum := bar - foo end");
        actual = parser.subprogram_declaration();
        actualString = actual.indentedToString(0);
        expectedString = "SubProgram: calculate, Return: REAL\n" + "Arguments: (foo [REAL], bar [REAL])\n" + "|-- Declarations\n" + "|-- --- Name: fub, Type: REAL\n" + "|-- --- Name: fum, Type: REAL\n" + "|-- SubProgramDeclarations\n" + "|-- --- SubProgram: dell, Return: null\n" + "|-- --- Arguments: (fi [REAL], croix [REAL])\n" + "|-- --- --- Declarations\n" + "|-- --- --- --- Name: soft, Type: INTEGER\n" + "|-- --- --- SubProgramDeclarations\n" + "|-- --- --- Compound Statement\n" + "|-- --- --- --- Assignment:\n" + "|-- --- --- --- --- Name: fi, Type: REAL\n" + "|-- --- --- --- --- Operation: PLUS, Type: REAL\n" + "|-- --- --- --- --- --- Name: fi, Type: REAL\n" + "|-- --- --- --- --- --- Name: croix, Type: REAL\n" + "|-- Compound Statement\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fub, Type: REAL\n" + "|-- --- --- Operation: PLUS, Type: REAL\n" + "|-- --- --- --- Name: foo, Type: REAL\n" + "|-- --- --- --- Name: bar, Type: REAL\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fum, Type: REAL\n" + "|-- --- --- Operation: MINUS, Type: REAL\n" + "|-- --- --- --- Name: bar, Type: REAL\n" + "|-- --- --- --- Name: foo, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("subprogram_declaration test 3 passed.");
    }

    @Test
    void declarations() {
        Parser parser = new Parser("var foo, bar : integer ; var fub : real ;");
        DeclarationsNode actual = parser.declarations();
        String actualString = actual.indentedToString(0);
        String expectedString = "Declarations\n" + "|-- Name: foo, Type: INTEGER\n" + "|-- Name: bar, Type: INTEGER\n" + "|-- Name: fub, Type: REAL\n";

        assertEquals(expectedString, actualString);
        System.out.println("declarations test 1 passed.");
    }

    @Test
    void program() {
        Parser parser = new Parser(new File("src/parser/test/simple.pas"));
        ProgramNode actual = parser.program();
        String actualString = actual.indentedToString(0);
        String expectedString = "Program: foo\n" + "|-- Declarations\n" + "|-- --- Name: fee, Type: INTEGER\n" + "|-- --- Name: fi, Type: INTEGER\n" + "|-- --- Name: fo, Type: INTEGER\n" + "|-- --- Name: fum, Type: INTEGER\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fee, Type: INTEGER\n" + "|-- --- --- Value: 4, Type: INTEGER\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fi, Type: INTEGER\n" + "|-- --- --- Value: 5, Type: INTEGER\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fo, Type: INTEGER\n" + "|-- --- --- Operation: PLUS, Type: INTEGER\n" + "|-- --- --- --- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- --- --- --- Value: 3, Type: INTEGER\n" + "|-- --- --- --- --- Name: fee, Type: INTEGER\n" + "|-- --- --- --- Name: fi, Type: INTEGER\n" + "|-- --- If:\n" + "|-- --- --- Operation: LTHAN, Type: INTEGER\n" + "|-- --- --- --- Name: fo, Type: INTEGER\n" + "|-- --- --- --- Value: 13, Type: INTEGER\n" + "|-- --- Then:\n" + "|-- --- --- Assignment:\n" + "|-- --- --- --- Name: fo, Type: INTEGER\n" + "|-- --- --- --- Value: 13, Type: INTEGER\n" + "|-- --- Else:\n" + "|-- --- --- Assignment:\n" + "|-- --- --- --- Name: fo, Type: INTEGER\n" + "|-- --- --- --- Value: 26, Type: INTEGER\n";
        assertEquals(expectedString, actualString);
        System.out.println("Program test 1 passed.");

        parser = new Parser(new File("src/parser/test/sample.pas"));
        actual = parser.program();
        actualString = actual.indentedToString(0);
        expectedString = "Program: foo\n" + "|-- Declarations\n" + "|-- --- Name: fee, Type: INTEGER\n" + "|-- --- Name: fi, Type: INTEGER\n" + "|-- --- Name: fo, Type: REAL\n" + "|-- --- Name: fum, Type: REAL\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fee, Type: INTEGER\n" + "|-- --- --- Operation: PLUS, Type: REAL\n" + "|-- --- --- --- Name: fee, Type: INTEGER\n" + "|-- --- --- --- Name: fo, Type: REAL\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: fo, Type: REAL\n" + "|-- --- --- Operation: PLUS, Type: REAL\n" + "|-- --- --- --- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- --- --- --- Value: 3, Type: INTEGER\n" + "|-- --- --- --- --- Name: fee, Type: INTEGER\n" + "|-- --- --- --- Name: fi, Type: INTEGER\n" + "|-- --- If:\n" + "|-- --- --- Operation: LTHAN, Type: REAL\n" + "|-- --- --- --- Name: fo, Type: REAL\n" + "|-- --- --- --- Value: 13, Type: INTEGER\n" + "|-- --- Then:\n" + "|-- --- --- Assignment:\n" + "|-- --- --- --- Name: fo, Type: REAL\n" + "|-- --- --- --- Value: 13, Type: REAL\n" + "|-- --- Else:\n" + "|-- --- --- Assignment:\n" + "|-- --- --- --- Name: fo, Type: REAL\n" + "|-- --- --- --- Value: 26, Type: REAL\n";
        assertEquals(expectedString, actualString);
        System.out.println("Program test 2 passed.");

        parser = new Parser(new File("src/parser/test/money.pas"));
        actual = parser.program();
        actualString = actual.indentedToString(0);
        expectedString = "Program: sample\n" + "|-- Declarations\n" + "|-- --- Name: dollars, Type: INTEGER\n" + "|-- --- Name: yen, Type: INTEGER\n" + "|-- --- Name: bitcoins, Type: INTEGER\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: dollars, Type: INTEGER\n" + "|-- --- --- Value: 1000000, Type: INTEGER\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: yen, Type: INTEGER\n" + "|-- --- --- Operation: ASTERISK, Type: INTEGER\n" + "|-- --- --- --- Name: dollars, Type: INTEGER\n" + "|-- --- --- --- Value: 114, Type: INTEGER\n" + "|-- --- Assignment:\n" + "|-- --- --- Name: bitcoins, Type: INTEGER\n" + "|-- --- --- Operation: FSLASH, Type: INTEGER\n" + "|-- --- --- --- Name: dollars, Type: INTEGER\n" + "|-- --- --- --- Value: 1184, Type: INTEGER\n";
        assertEquals(expectedString, actualString);
        System.out.println("Program test 3 passed.");
    }
*/
}