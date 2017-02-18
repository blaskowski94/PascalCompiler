package compiler;

import parser.Parser;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 3rd, 2017
 * <p>
 * Main driver for the compiler.
 */

public class CompilerMain {

    public static void main(String[] args) {
        Parser parser = new Parser("src/parser/test/simple.pas", true);
        parser.program();
        System.out.println("Yes");
        System.out.println(parser.getSymbolTable().toString());
    }
}
