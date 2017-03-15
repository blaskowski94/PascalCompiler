package parser.test;

import org.junit.jupiter.api.Test;
import parser.Parser;
import syntaxtree.ExpressionNode;
import syntaxtree.ProgramNode;

import static org.junit.Assert.assertEquals;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * March 14th, 2017
 * <p>
 * This class uses the JUnit framework to test the Parser class now that it creates a syntax tree. For testing purposes
 * the input file "simple.pas" is uesd to test parsing a whole program as well as smaller Strings to test specific
 * aspects of the parser.
 *
 * @author Bob Laskowski
 */
class ParserTest2 {

    @Test
    void factor() {
        // Just a value
        Parser parser = new Parser("4", false);
        ExpressionNode actual = parser.factor();
        String actualString = actual.indentedToString(0);
        String expectedString = "Value: 4\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 1 passed.");

        // Just an id
        parser = new Parser("foo", false);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Name: foo\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 2 passed.");

        // Array id
        parser = new Parser("foo[4*5]", false);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Array\n" + "Name: foo\n" + "|-- Operation: ASTERISK\n" + "|-- --- Value: 4\n" + "|-- --- Value: 5\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 3 passed.");

        // Procedure? id
        // Doesn't work correctly
//        parser = new Parser( "foo(4*5)", false);
//        actual = parser.factor();
//        actualString = actual.indentedToString( 0);
//        expectedString = "Name: foo\n";
//
//        assertEquals( expectedString, actualString);
//        System.out.println("Factor test 4 passed.");

        // expression in paren
        // Doesn't work correctly
//        parser = new Parser( "(4*5)", false);
//        actual = parser.factor();
//        actualString = actual.indentedToString( 0);
//        expectedString = "Name: foo\n";
//
//        assertEquals( expectedString, actualString);
//        System.out.println("Factor test 5 passed.");

        // not factor
        parser = new Parser("not 4", false);
        actual = parser.factor();
        actualString = actual.indentedToString(0);
        expectedString = "Operation: NOT\n" + "|-- Value: 4\n";

        assertEquals(expectedString, actualString);
        System.out.println("Factor test 6 passed.");
    }

    // change to tests of simple expressions that cannot just be factors
    @Test
    void simple_expression() {
        // Just a value
        Parser parser = new Parser("4", false);
        ExpressionNode actual = parser.simple_expression();
        String actualString = actual.indentedToString(0);
        String expectedString = "Value: 4\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 1 passed.");

        // Just an id
        parser = new Parser("foo", false);
        actual = parser.simple_expression();
        actualString = actual.indentedToString(0);
        expectedString = "Name: foo\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 2 passed.");

        // Array id
        parser = new Parser("foo[4*5]", false);
        actual = parser.simple_expression();
        actualString = actual.indentedToString(0);
        expectedString = "Array\n" + "Name: foo\n" + "|-- Operation: ASTERISK\n" + "|-- --- Value: 4\n" + "|-- --- Value: 5\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 3 passed.");

        // Procedure? id
        // Doesn't work correctly
//        parser = new Parser( "foo(4*5)", false);
//        actual = parser.simple_expression();
//        actualString = actual.indentedToString( 0);
//        expectedString = "Name: foo\n";
//
//        assertEquals( expectedString, actualString);
//        System.out.println("simple_expression test 4 passed.");

        // expression in paren
        // Doesn't work correctly
//        parser = new Parser( "(4*5)", false);
//        actual = parser.simple_expression();
//        actualString = actual.indentedToString( 0);
//        expectedString = "Name: foo\n";
//
//        assertEquals( expectedString, actualString);
//        System.out.println("simple_expression test 5 passed.");

        // not factor
        // Not sure if this works
        parser = new Parser("not 4", false);
        actual = parser.simple_expression();
        actualString = actual.indentedToString(0);
        expectedString = "Operation: NOT\n" + "|-- Value: 4\n";

        assertEquals(expectedString, actualString);
        System.out.println("simple_expression test 6 passed.");
    }

    @Test
    void statement() {

    }

    @Test
    void subprogram_declaration() {

    }

    @Test
    void declarations() {

    }

    @Test
    void program() {
        Parser parser = new Parser("src/parser/test/simple.pas", true);
        ProgramNode actual = parser.program();
        String actualString = actual.indentedToString(0);
        String expectedString = "Program: foo\n" + "|-- Declarations\n" + "|-- --- Name: fee\n" + "|-- --- Name: fi\n" + "|-- --- Name: fo\n" + "|-- --- Name: fum\n" + "|-- SubProgramDeclarations\n" + "|-- Compound Statement\n" + "|-- --- Assignment\n" + "|-- --- --- Name: fee\n" + "|-- --- --- Value: 4\n" + "|-- --- Assignment\n" + "|-- --- --- Name: fi\n" + "|-- --- --- Value: 5\n" + "|-- --- Assignment\n" + "|-- --- --- Name: fo\n" + "|-- --- --- Operation: PLUS\n" + "|-- --- --- --- Operation: ASTERISK\n" + "|-- --- --- --- --- Value: 3\n" + "|-- --- --- --- --- Name: fee\n" + "|-- --- --- --- Name: fi\n" + "|-- --- If\n" + "|-- --- --- Operation: LTHAN\n" + "|-- --- --- --- Name: fo\n" + "|-- --- --- --- Value: 13\n" + "|-- --- --- Assignment\n" + "|-- --- --- --- Name: fo\n" + "|-- --- --- --- Value: 13\n" + "|-- --- --- Assignment\n" + "|-- --- --- --- Name: fo\n" + "|-- --- --- --- Value: 26\n";
        assertEquals(expectedString, actualString);
        System.out.println("Program test passed.");
    }

}