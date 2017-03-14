#DESCRIPTION
This project will eventually be a Mini-Pascal to MIPS assembly compiler. It is written in Java. So far, the scanner, parser and symbol table have been implemented. Right now, the program will parse mini-pascal code using the scanner I have built, add all of the variables, programs, functions, procedures and array declared to a global symbol table, and determine whether or not the code is a valid mini-pascal program.

The program can be run from the command line by compiling all the .java files with javac and then running:

java CompilerMain input.txt

where input.txt is any text file that you wish to parse. The program will print out the Tokens as they are matched and whether or not it parsed successfully.

