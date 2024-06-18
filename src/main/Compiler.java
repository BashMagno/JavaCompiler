package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Compiler {

    private static final String EXTENSION = ".ahm";

    public static boolean isFile(String file) {
        return file.endsWith(EXTENSION);
    }

    public static void main(String[] args) {

        // Checking the length of arguments given
        if (args.length != 1) {
            ErrorHandler.getError(ErrorHandler.ERROR_INIT_ONE);
            System.exit(1);
        }

        // Creating String variable to assign the path of the .ahm file in it, otherwise shows up an error and exits. ERROR 1
        String file = args[0];

        // Verifying file has the .ahm extension, otherwise shows up an error and exits. ERROR 2
        if (!isFile(file)) {
            ErrorHandler.getError(ErrorHandler.ERROR_INIT_TWO);
            System.exit(2);
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            Lexer lexer = new Lexer(content);
            
            List<Token> tokens = lexer.tokenize();

            Interpreter interpreter = new Interpreter();
            interpreter.execute(tokens);

            // Print results after execution
            for (Object result : interpreter.getResults()) {
                System.out.println(result);
            }
            Token.printTokens(tokens);   //Printing Tokens type and lexeme (Debug Mode)
            
            // Assembly code example generator
            Assembly.generateAssembly(tokens);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
