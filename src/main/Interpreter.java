package src.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private Map<String, Object> variables = new HashMap<>();
    private List<Object> results = new ArrayList<>();
    private List<Token> tokens;

    public List<Object> getResults() {
        return results;
    }

    public void execute(List<Token> tokens) {
        this.tokens = tokens;

        for (int i = 0; i < tokens.size(); i++) {
            executeStatement(i);
        }

    }

    private void executeStatement(int index) {
        Token token = tokens.get(index);
        switch (token.getType()) {
            case INT:
                executeVariableDeclaration(index);
                break;
            case STRING:
                executeVariableDeclaration(index);
                break;
            case ADD:
                executeAdditionExpression(index);
                break;
            case FOR:
            /* executeForLoop(index);           */
                break;
            case WHILE:
            /* executeWhileLoop(index);         */
                break;
            case IF:
            /* executeIfStatement(index);       */
                break;
            case PRINT:
                print(index);
                break;
            // Mostrar los resultados al final de la ejecución
            case RETURN:
                executeReturnStatement(index);
                break;
            default:
                // Otros casos según sea necesario
                System.out.println("No es ningun caso");
                break;
        }
    }

    private Object parseValue(int index) 
    {
        index++; // Avanzar al siguiente token después del '='
        if (index < tokens.size()) 
        {
            Token valueToken = tokens.get(index);
            switch (valueToken.getType()) 
            {
                case NUMBER:
                    try {
                        return Integer.parseInt(valueToken.getLexeme());
                    } catch (NumberFormatException e) 
                    {
                        System.out.println("Error: No se pudo convertir el número '" + valueToken.getLexeme() + "'");
                    }
                    break;
                case STRING_LITERAL:
                    try 
                    {
                        return valueToken.getLexeme();
                    } catch (Exception e) 
                    {
                        System.out.println("Error: Error parsing lexeme to String");
                    }
                case IDENTIFIER:
                    try {
                        String variableName = valueToken.getLexeme();
                        if (variables.containsKey(variableName)) {
                            return variables.get(variableName);
                        } else {
                            System.out.println("Error: La variable '" + variableName + "' no está definida");
                        }
                    } catch (Exception e) {
                        System.out.println("Error: Error al obtener el valor de la variable");
                    }
                break;
                default:
                    // Otros casos según sea necesario
                    System.out.println("Error: Tipo de valor no reconocido: " + valueToken.getType());
                    break;
            }
        } else {
            // Error: Fin de tokens inesperado después de la declaración de variable
            System.out.println("Error: Fin de tokens inesperado después de la declaración de variable");
        }
        return null;
    }
    

    private void executeVariableDeclaration(int index) {
        String variableName = tokens.get(index + 1).getLexeme();
        if (variables.containsKey(variableName)) {
            // La variable ya está definida
            System.out.println("Error: La variable '" + variableName + "' ya está definida");
        } else {
            // Avanzar al siguiente token después del '='
            index += 2;
            if (index < tokens.size()) {
                Token valueToken = tokens.get(index);
                Object value = parseValue(index);
                variables.put(variableName, value);
            } else {
                // Error: Fin de tokens inesperado después de la declaración de variable
                System.out.println("Error: Fin de tokens inesperado después de la declaración de variable");
            }
        }
    }

    //  OPERATIONS

    private int executeAdditionExpression(int index) {
        int result = 0;
        boolean firstOperand = true;
    
        while (index < tokens.size()) 
        {
            Token currentToken = tokens.get(index);
    
            if (currentToken.getType() == TokenType.NUMBER) 
            {
                int operand = Integer.parseInt(currentToken.getLexeme());
                result = (firstOperand) ? operand : result + operand;
                firstOperand = false;
            } else if (currentToken.getType() == TokenType.IDENTIFIER) 
            {
                String variableName = currentToken.getLexeme();
                if (variables.containsKey(variableName)) 
                {
                    int operand = (int) variables.get(variableName);
                    result = (firstOperand) ? operand : result + operand;
                    firstOperand = false;
                } else {
                    System.out.println("Error: La variable '" + variableName + "' no está definida");
                    return 0;
                }
            } else if (currentToken.getType() == TokenType.ADD) 
            {
                // Avanzar al siguiente token después del '+'
                index++;
            } else if (currentToken.getType() == TokenType.SEMICOLON) 
            {
                // Fin de la declaración
                break;
            }
    
            index++; // Avanzar al siguiente token
        }
    
        return result;
    }
    
    private void executeReturnStatement(int index)
    {
        index++; // Avanzar al siguiente token después de "return"
        if (index < tokens.size()) {
            Token valueToken = tokens.get(index);
            switch (valueToken.getType()) {
                case IDENTIFIER:
                    // Si el token es un identificador, intenta obtener su valor
                    String variableName = valueToken.getLexeme();
                    if (variables.containsKey(variableName)) {
                        Object value = variables.get(variableName);
                        results.add(value);
                    } else 
                    {
                        System.out.println("Error: La variable '" + variableName + "' no está definida");
                    }
                    break;
                case STRING:
                    String variableNameString = valueToken.getLexeme();
                    if (variables.containsKey(variableNameString)) 
                    {
                        Object value = variables.get(variableNameString);
                        results.add(value);
                    }
                    break;
                case NUMBER:
                    // Si el token es un número, simplemente agrega su valor a los resultados
                    results.add(Integer.parseInt(valueToken.getLexeme()));
                    break;
                case CHAR_LITERAL:
                    results.add(valueToken.getLexeme().toCharArray()[1]);
                    break;
                case FLOAT_LITERAL:
                    results.add(Float.parseFloat(valueToken.getLexeme()));
                    break;
                case BOOL_LITERAL:
                    results.add(Boolean.parseBoolean(valueToken.getLexeme()));
                    break;
                case STRING_LITERAL:
                    results.add(valueToken.getLexeme().replace("\"", ""));
                    break;
                default:
                    // Otros casos según sea necesario
                    System.out.println("Error: Tipo de valor no reconocido en la declaración de retorno");
                    break;
            }
        } else {
            // Error: Fin de tokens inesperado después de la declaración de retorno
            System.out.println("Error: Fin de tokens inesperado después de la declaración de retorno");
        }
    }

    private void print(int index) {
        index += 2; // Avanzar al siguiente token después de '('
    
        if (index < tokens.size()) {
            Token variableToken = tokens.get(index);
            String variableName = variableToken.getLexeme();
            if (variables.containsKey(variableName)) {
                Object value = variables.get(variableName);
                System.out.println("Debug: Print statement - Value: " + value);
                System.out.println(value);
                // Puedes almacenar el valor en una lista de resultados si es necesario
                results.add(value);
            } else {
                // Error: La variable no está definida
                System.out.println("Error: La variable '" + variableName + "' no está definida");
            }
        } else {
            // Error: Fin de tokens inesperado después de la declaración de impresión
            System.out.println("Error: Fin de tokens inesperado después de la declaración de impresión");
        }
    }
}









