package parser.test;

import org.junit.jupiter.api.Test;
import parser.Parser;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * February 3rd, 2017
 * <p>
 * This class uses the JUnit framework to test the Parser class. For testing purposes the input file "simple.pas"
 * to test parsing a whole program as well as smaller Strings to test specific aspects of the parser.
 *
 * @author Bob Laskowski
 */
class ParserTest {

    /**
     * This tests the entire parser on a complete mini-pascal program using the top level function program. The
     * mini-pascal program used is simple.pas which can be found in parser < test.
     */
    @Test
    void program() {
        System.out.println("-----program-----");
        String input = "src/parser/test/simple.pas";
        System.out.println("File to be parsed: " + input);
        Parser instance = new Parser(input, true);
        instance.program();
        System.out.println("It Parsed!\n");
    }

    /**
     * This tests the declaration function in the parser. Per the grammar, a single declaration would have the format:
     * <p>
     * var fee: integer;
     * <p>
     * Multiple declarations would be:
     * <p>
     * var fee, fi, fo: real;
     * <p>
     * These two strings will be used to test declarations.
     */
    @Test
    void declarations() {
        System.out.println("-----declarations-----");
        String input = "var fee: integer;";
        System.out.println("String to be parsed: " + input);
        Parser instance = new Parser(input, false);
        instance.declarations();
        System.out.println("It Parsed!\n");

        input = "var fee, fi, fo: real;";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.declarations();
        System.out.println("It Parsed!\n");
    }

    /**
     * This tests the subprogram_declaration function in the parser. Per the grammar, a subprogram_declaration would
     * have the format:
     * <p>
     * function doSomething(fee: integer): integer;
     * var fo: integer;
     * begin
     * fo := 2*fee
     * end
     * <p>
     * Another subprogram declaration would be:
     * <p>
     * function doSomething(fee, fi: integer): integer;
     * var fo: integer;
     * function doSomethingElse: integer;
     * begin
     * fo := fee + fi;
     * fo := 2
     * end
     * ;
     * begin
     * fo := 2 * fo
     * end
     * <p>
     * These two strings will be used to test declarations.
     */
    @Test
    void subprogram_declaration() {
        System.out.println("-----subprogram_declaration-----");
        String input = "function doSomething(fee: integer): integer; " + "var fo: integer; " + "begin " + "fo := 2*fee " + "end";
        System.out.println("String to be parsed: " + input);
        Parser instance = new Parser(input, false);
        instance.subprogram_declaration();
        System.out.println("It Parsed!\n");

        input = "function doSomething(fee, fi: integer): integer; " + "var fo: integer; " + "function doSomethingElse: integer; " + "begin " + "fo := fee + fi; " + "fo := 2 " + "end " + "; " + "begin " + "fo := 2 * fo " + "end";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.subprogram_declaration();
        System.out.println("It Parsed!\n");
    }


    /**
     * This tests the statement function in the parser. Per the grammar, a statement could take the following formats:
     * <p>
     * foo := 4
     * <p>
     * foo := 4*5
     * <p>
     * begin
     * if foo < 5
     * then fi := 5
     * else fi := 10
     * end
     * <p>
     * while foo < 5
     * do foo := foo + 1
     * <p>
     * <p>
     * These four strings will be used to test declarations.
     * <p>
     * Note that procedure_statement, read(id) and write(id) have not yet been implemented
     */
    @Test
    void statement() {
        System.out.println("-----statement-----");
        String input = "foo := 4";
        System.out.println("String to be parsed: " + input);
        Parser instance = new Parser(input, false);
        instance.statement();
        System.out.println("It Parsed!\n");

        input = "foo := 4*5";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.statement();
        System.out.println("It Parsed!\n");

        input = "begin " + "if foo < 5 " + "then fi := 5 " + "else fi := 10 " + "end";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.statement();
        System.out.println("It Parsed!\n");

        input = "while foo < 5 " + "do foo := foo + 1";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.statement();
        System.out.println("It Parsed!\n");
    }

    /**
     * This tests the simple_expression function in the parser. Per the grammar, a simple_expression could take the
     * following formats:
     * <p>
     * foo + fi
     * <p>
     * -4
     * <p>
     * foo[foo+fi]
     * <p>
     * foo(foo+fi, fi-fo)
     * <p>
     * (foo+fi)
     * <p>
     * (4+5)
     * <p>
     * not foo
     * <p>
     * These seven strings will be used to test declarations.
     */
    @Test
    void simple_expression() {
        System.out.println("-----simple_expression-----");
        String input = "foo + fi";
        System.out.println("String to be parsed: " + input);
        Parser instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "-4";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "foo[foo+fi]";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "foo(foo+fi, fi-fo)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "(foo+fi)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "(4+5)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "not foo";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");
    }

    /**
     * This tests the factor function in the parser. Per the grammar, a factor could take the following formats:
     * <p>
     * foo
     * <p>
     * foo[fe < fo]
     * <p>
     * foo(fe < fo, fu <> fum)
     * <p>
     * 4
     * <p>
     * (foo+fi)
     * <p>
     * (4+5)
     * <p>
     * not foo
     * <p>
     * These seven strings will be used to test declarations.
     */
    @Test
    void factor() {
        System.out.println("-----factor-----");
        String input = "foo";
        System.out.println("String to be parsed: " + input);
        Parser instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "foo[fe < fo]";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "foo(fe < fo, fu <> fum)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "4";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "(foo+fi)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "(4+5)";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");

        input = "not foo";
        System.out.println("String to be parsed: " + input);
        instance = new Parser(input, false);
        instance.simple_expression();
        System.out.println("It Parsed!\n");
    }


}