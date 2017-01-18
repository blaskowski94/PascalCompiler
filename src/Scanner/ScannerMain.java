package Scanner;

/**
 * Bob Laskowski
 * Compilers I
 * Dr. Erik Steinmetz
 * December 13th, 2016
 * <p>
 * This class contains the main method that drives our scanner. It takes in a text file to parse as a command line
 * argument. A FileInputStream and an InputStreamReader are used to read from the file. A MyScanner object is created
 * using our scanner definition defined in Scanner.flex. The scanner is designed to parse Mini-pascal.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScannerMain {

    public static void main(String[] args) {
        // The file from the command line argument
        String filename = args[0];

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
        MyScanner scanner = new MyScanner(isr);

        // Iterate through the file one lexeme at a time until end of file is reached
        Token aToken = null;
        do {
            try {
                aToken = scanner.nextToken();
                if (aToken != null)
                    System.out.println("Token - " + aToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (aToken != null);
        try {
            scanner.yyclose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}