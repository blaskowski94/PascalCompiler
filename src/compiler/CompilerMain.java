package compiler;

import codefolding.CodeFolding;
import codegeneration.CodeGeneration;
import parser.Parser;
import symboltable.SymbolTable;
import syntaxtree.ProgramNode;

import java.io.*;

/**
 * Bob Laskowski,
 * Compilers II,
 * Dr. Erik Steinmetz,
 * February 3rd, 2017
 * <p>
 * Main driver for the compiler. Run with a mini-pascal program to parse as the only command line argument, "help"/"-h"
 * for instructions, or no command line arguments for a sample program to be parsed. This sample program can be found
 * in src/parser/test/simple.pas.
 *
 * Output will be two text files, foo.table and foo.tree with the symbol table and syntax tree.
 * @author Bob Laskowski
 */

public class CompilerMain {

    public static void main(String[] args) {
        boolean help = false;
        File program = null;
        // Default program to use if no command line arguments
        if (args.length == 0) {
            program = new File("src/parser/test/example.pas");
        }
        // If one argument passed in, use that as program or help()
        else if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("-help")) {
                help();
                help = true;
            } else program = new File(args[0]);
        }
        // If more than one argument, print error
        else {
            System.out.println("Please enter program to parse as the only command line argument or enter \"-h\" for help.");
            System.exit(1);
        }

        if (!help) {
            Parser parser = new Parser(program);
            CodeFolding cf = new CodeFolding();
            ProgramNode tree = parser.program();
            int original = tree.indentedToString(0).split("\n").length;
            //System.out.println(tree.indentedToString(0));
            cf.foldProgram(tree);
            int end = tree.indentedToString(0).split("\n").length;
            //System.out.println(tree.indentedToString(0));
            //System.out.println("Difference: " + (original - end));

            CodeGeneration cg = new CodeGeneration(tree, parser.getSymbolTable());
            String theCode = cg.writeCodeForRoot();
            writeToFile(tree, parser, program, theCode);
            System.out.println(theCode);
            //String assemblyFileName = filename + ".asm";
            // Write assembly to a file
        }
    }

    public static void help() {
        String help = "This program parses a Mini-pascal file.\n" + "To see an example, run with no command line arguments.\n" + "This will parse the \"simple.pas\" file. To parse your own file, run with the absolute or relative path of the file as the only command line argument.\n" + "The program will output two files, one with the syntax tree and the other with the contents of the symbol table. The will be named \"YourProgramName.tree\"" + "and \"YourProgramName.table and located in the output folder of the compilers package.\"\n\n" + "Example usage:\njava CompilerMain src/parser/test/simple.pas";
        System.out.println(help);
    }

    public static void writeToFile(ProgramNode program, Parser pars, File f, String code) {
        SymbolTable STC = pars.getSymbolTable();
        String name;
        try {
            name = f.getName().split("[.]")[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            error("Invalid file name");
            name = "foo";
        }
        // Write syntax tree and contents of symbol table to files
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/compiler/output/" + name + ".tree"), "utf-8"))) {
            writer.write(program.indentedToString(0));
        } catch (Exception ex) {
            error("Problem with tree output file.");
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/compiler/output/" + name + ".table"), "utf-8"))) {
            writer.write(STC.toString());
        } catch (Exception ex) {
            error("Problem with table output file.");
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/compiler/output/" + name + ".asm"), "utf-8"))) {
            writer.write(code);
        } catch (Exception ex) {
            error("Problem with assembly output file.");
        }
    }

    private static void error(String message) {
        System.out.println("Error: " + message);
        //System.exit(1);
    }
}
