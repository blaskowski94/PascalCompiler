package compiler;

import parser.Parser;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 3rd, 2017
 * <p>
 * Main driver for the compiler.
 * TODO: take in command line arguments with file, output simple.tree, simple.table
 */

public class CompilerMain {

    public static void main(String[] args) {
        String program = "";
        if (args.length == 0) {
            program = "src/parser/test/simple.pas";
        } else if (args.length == 1) {
            program = args[1];
        } else {
            System.out.println("Please enter program to parse as command line argument.");
            System.exit(1);
        }
        // Add error check
        Parser parser = new Parser(program, true);
        parser.program();
        //System.out.println(parser.getSymbolTable().toString());
    }
}
