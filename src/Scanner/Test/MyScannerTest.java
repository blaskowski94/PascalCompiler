package Scanner.Test;

import Scanner.MyScanner;
import Scanner.Token;
import Scanner.Type;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Bob Laskowski
 * Compilers II
 * Dr. Erik Steinmetz
 * January 17th, 2017
 * <p>
 * This class uses the JUnit framework to test the MyScanner class. For testing purposes the input file "simplest.pas"
 * is used. This file contains the simplest definition of a Pascal program as follows:
 * <p>
 * program foo;
 * begin
 * end
 * .
 * <p>
 * Before each test a new MyScanner object is created which is used for the tests.
 */

class MyScannerTest {

    private final String filename = "src/Scanner/Test/simplest.pas";
    private MyScanner scanner;

    /**
     * Create the MyScanner object using the filename stored as an instance variable.
     */
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // Create the FileInputStream
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the InputStreamReader
        InputStreamReader isr = new InputStreamReader(fis);

        // Create a MyScanner object
        scanner = new MyScanner(isr);
    }

    /**
     * Nullify the scanner object created.
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        scanner = null;
    }

    @org.junit.jupiter.api.Test
    void yyclose() {
    }

    @org.junit.jupiter.api.Test
    void yyreset() {

    }

    @org.junit.jupiter.api.Test
    void yystate() {

    }

    @org.junit.jupiter.api.Test
    void yybegin() {

    }

    @org.junit.jupiter.api.Test
    void yytext() {

    }

    @org.junit.jupiter.api.Test
    void yycharat() {

    }

    @org.junit.jupiter.api.Test
    void yylength() {

    }

    @org.junit.jupiter.api.Test
    void yypushback() {

    }

    /**
     * Tests whether the next token returned is the expected lexeme and Type.
     *
     * @throws IOException
     */
    @org.junit.jupiter.api.Test
    void nextToken() throws IOException {
        System.out.println("-----Test nextToken-----");
        String expLex = "program";
        System.out.println("Expected lex: " + expLex);
        Type expType = Type.PROGRAM;
        System.out.println("Expected type: " + expType);
        Token result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 1 pass.\n");

        expLex = "foo";
        System.out.println("Expected lex: " + expLex);
        expType = Type.ID;
        System.out.println("Expected type: " + expType);
        result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 2 pass.\n");

        expLex = ";";
        System.out.println("Expected lex: " + expLex);
        expType = Type.SEMI;
        System.out.println("Expected type: " + expType);
        result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 3 pass.\n");

        expLex = "begin";
        System.out.println("Expected lex: " + expLex);
        expType = Type.BEGIN;
        System.out.println("Expected type: " + expType);
        result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 4 pass.\n");

        expLex = "end";
        System.out.println("Expected lex: " + expLex);
        expType = Type.END;
        System.out.println("Expected type: " + expType);
        result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 5 pass.\n");

        expLex = ".";
        System.out.println("Expected lex: " + expLex);
        expType = Type.PERIOD;
        System.out.println("Expected type: " + expType);
        result = scanner.nextToken();
        System.out.println("Actual lex: " + result.getLexeme());
        System.out.println("Actual type: " + result.getType());
        assertEquals(expLex, result.getLexeme());
        assertEquals(expType, result.getType());
        System.out.println("Test case 6 pass.\n");

        System.out.println("All nextToken tests PASSED.\n");
    }

}