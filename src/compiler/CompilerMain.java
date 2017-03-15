package compiler;

import parser.Parser;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 3rd, 2017
 * <p>
 * Main driver for the compiler.
 * TODO: output simple.tree, simple.table
 */

public class CompilerMain {

    public static void main(String[] args) {
        boolean help = false;
        String program = "";
        // Default program to use if no command line arguments
        if (args.length == 0) {
            program = "src/parser/test/simple.pas";
        }
        // If one argument passed in, use that as program
        else if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("-help")) {
                help();
                help = true;
            } else program = args[0];
        }
        // If more than one argument, print error
        else {
            System.out.println("Please enter program to parse as the only command line argument.");
            System.exit(1);
        }

        if (!help) {
            Parser parser = new Parser(program, true);
            parser.program();
        }
    }

    public static void help() {
        String help = "This program parses a Mini-pascal file.\n" + "To see an example, run with no command line arguments.\n" + "This will parse the \"simple.pas\" file. To parse your own file, run with the absolute or relative path of the file as the only command line argument.\n" + "The program will output two files, one with the syntax tree and the other with the contents of the symbol table. The will be named \"YourProgramName.tree\"" + "and \"YourProgramName.table and located in the output folder of the compilers package.\"\n\n" + "Example usage:\njava CompilerMain src/parser/test/simple.pas";
        System.out.println(help);
    }
}
