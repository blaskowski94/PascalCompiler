package scanner.test;

import scanner.MyScanner;
import scanner.Token;
import scanner.Type;

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
 * is used. This file uses the simplest definition of a Pascal program as follows from an input file:
 * <p>
 * program foo;
 * begin
 * end
 * .
 * <p>
 * Before each test a new MyScanner object is created which is used for the tests.
 */

class MyScannerTest {

    private final String filename = "src/scanner/test/simplest.pas";
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

    /**
     * Tests whether the yytext function returns the correct lexeme for each token of a program. nextToken must be called
     * before each call to yytext to advance the scanner to the next token. Before the first call and after the last
     * call, yytext should return an empty String.
     *
     * @throws IOException if any I/O-Error occurs
     */
    @org.junit.jupiter.api.Test
    void yytext() throws IOException {
        System.out.println("-----test yytext-----");

        String expRes = "";
        System.out.println("Expected result: " + expRes);
        String result = scanner.yytext();
        System.out.println("Actual result: " + result);
        assertEquals(expRes, result);
        System.out.println("test case 1 passed.\n");

        scanner.nextToken();

        expRes = "program";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 2 passed.\n");

        scanner.nextToken();

        expRes = "foo";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 3 passed.\n");

        scanner.nextToken();

        expRes = ";";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 4 passed.\n");

        scanner.nextToken();

        expRes = "begin";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 5 passed.\n");

        scanner.nextToken();

        expRes = "end";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 6 passed.\n");

        scanner.nextToken();

        expRes = ".";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 7 passed.\n");

        scanner.nextToken();

        expRes = "";
        System.out.println("Expected result: " + expRes);
        result = scanner.yytext();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 8 passed.\n");
    }

    /**
     * Tests whether the next token returned is the expected lexeme and Type.
     *
     * @throws IOException if any I/O-Error occurs
     */
    @org.junit.jupiter.api.Test
    void nextToken() throws IOException {
        System.out.println("-----test nextToken-----");
        Token expRes = new Token("program", Type.PROGRAM, 0);
        System.out.println("Expected result: " + expRes);
        Token result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 1 pass.\n");

        expRes = new Token("foo", Type.ID, 0);
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 2 pass.\n");

        expRes = new Token(";", Type.SEMI, 0);
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 3 pass.\n");

        expRes = new Token("begin", Type.BEGIN, 0);
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 4 pass.\n");

        expRes = new Token("end", Type.END, 0);
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 5 pass.\n");

        expRes = new Token(".", Type.PERIOD, 0);
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 6 pass.\n");

        // EOF value should be null
        expRes = null;
        System.out.println("Expected result: " + expRes);
        result = scanner.nextToken();
        System.out.println("Actual result:   " + result);
        assertEquals(expRes, result);
        System.out.println("test case 7 pass.\n");

        System.out.println("All nextToken tests PASSED.\n");
    }

}