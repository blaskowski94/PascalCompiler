# DESCRIPTION
This project is a Mini-Pascal to MIPS assembly compiler. It is written in Java. The program parses an input pascal file utilizing its own custom scanner, which was generated using JFlex. The parser generates a symbol table and syntax tree for the pascal program. The syntax tree generated has code folding performed on it and is then traversed to generate the assembly code. The MIPS assembly can be run using the QtSpim simulator. 

The program can be run from the command line using the compiled .jar file located in the product folder. Sample mini-pascal files can be found in src/pascalfiles. Use the following command while in the same directory as the .jar:

java -jar compiler.jar money.pas

Note: make sure your .pas file is in the same directory as well or include the path to it.

A .asm file with the same name as your mini-pascal file will then be generated in the same folder. Load that file into QtSpim and run.

