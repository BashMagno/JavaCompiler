package main;

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
            i = executeStatement(i);
        }
    }

    private int executeStatement(int index) {
        Token token = tokens.get(index);
        switch (token.getType()) {
            case VAR:
                index = executeVariableDeclaration(index);
                break;
            case FOR:
                index = executeForStatement(index);
                break;
            case IF:
                index = executeIfStatement(index);
                break;
            case PRINT:
                index = executePrint(index);
                break;
            case RETURN:
                index = executeReturnStatement(index);
                break;
            default:
                break;
        }
        return index;
    }

    private int executeForStatement(int index) {
        // Avanzar al token de la variable de iteración
        index += 3; // Se asume que la estructura del for es "for (var i = ...)"
        String variableName = tokens.get(index).getLexeme();
    
        // Avanzar al token de la asignación de la variable de iteración
        index += 2; // Se asume que la estructura del for es "for (var i = 0;"
        Object initialValue = evaluateExpression(index);
        if (!(initialValue instanceof Integer)) {
            System.out.println("Error: El valor inicial del bucle for debe ser un número entero");
            return index;
        }
        int value = (int) initialValue;
        variables.put(variableName, value);
    
        // Avanzar al token de la condición de salida del bucle
        while (tokens.get(index).getType() != TokenType.SEMICOLON) {
            index++;
        }
    
        // Avanzar al token del incremento o decremento de la variable de iteración
        index++;
        int incrementIndex = index;
        while (tokens.get(index).getType() != TokenType.RIGHT_PAREN) {
            index++;
        }
    
        // Buscar el inicio del bloque del bucle
        int blockStartIndex = -1;
        for (int i = index + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.LEFT_BRACE) {
                blockStartIndex = i;
                break;
            }
        }
    
        // Ejecutar el bucle for
        for (; (boolean) evaluateExpression(incrementIndex); executeStatement(index + 1)) {
            int newIndex = executeBlock(blockStartIndex + 1);
            if (newIndex == tokens.size()) {
                break; // Terminó la ejecución con return
            }
            // Evaluar el incremento o decremento de la variable de iteración
            executeStatement(incrementIndex);
        }
    
        // Saltar el bloque después de que la condición se vuelve falsa
        while (tokens.get(index).getType() != TokenType.RIGHT_BRACE) {
            index++;
        }
        return index + 1; // Devolver el índice después del bloque
    }
    
    
    
    
    

    private Object evaluateExpression(int index) {
        List<Object> values = new ArrayList<>();
        List<TokenType> operators = new ArrayList<>();
        boolean isInsideParentheses = false;
    
        while (index < tokens.size()) {
            Token currentToken = tokens.get(index);
            switch (currentToken.getType()) {
                case LEFT_PAREN:
                    isInsideParentheses = true;
                    break;
                case RIGHT_PAREN:
                    isInsideParentheses = false;
                    break;
                case NUMBER:
                    values.add(Integer.parseInt(currentToken.getLexeme()));
                    break;
                case IDENTIFIER:
                    if (variables.containsKey(currentToken.getLexeme())) {
                        values.add(variables.get(currentToken.getLexeme()));
                    } else {
                        System.out.println("Error: La variable '" + currentToken.getLexeme() + "' no está definida");
                        return false; // Devolver false en caso de error
                    }
                    break;
                case ADD:
                case SUBSTRAIN:
                case MULTIPLY:
                case DIVIDE:
                case GREATER_THAN:
                case LESS_THAN:
                    if (!isInsideParentheses) {
                        operators.add(currentToken.getType());
                    }
                    break;
                case SEMICOLON:
                case LEFT_BRACE:
                case RIGHT_BRACE:
                    return evaluateValuesAndOperators(values, operators);
                default:
                    break;
            }
            index++;
        }
        return evaluateValuesAndOperators(values, operators);
    }
    
    

    private Object evaluateValuesAndOperators(List<Object> values, List<TokenType> operators) {
        while (!operators.isEmpty()) {
            for (int i = 0; i < operators.size();) {
                TokenType operator = operators.get(i);
                Object leftValue = values.get(i);
                Object rightValue = values.get(i + 1);
                Object result = 0;

                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    int leftInt = (int) leftValue;
                    int rightInt = (int) rightValue;

                    switch (operator) {
                        case ADD:
                            result = leftInt + rightInt;
                            break;
                        case SUBSTRAIN:
                            result = leftInt - rightInt;
                            break;
                        case MULTIPLY:
                            result = leftInt * rightInt;
                            break;
                        case DIVIDE:
                            if (rightInt != 0) {
                                result = leftInt / rightInt;
                            } else {
                                System.out.println("Error: División por cero");
                                return 0;
                            }
                            break;
                        case GREATER_THAN:
                            result = leftInt > rightInt;
                            break;
                        case LESS_THAN:
                            result = leftInt < rightInt;
                            break;
                        default:
                            break;
                    }
                } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
                    boolean leftBool = (boolean) leftValue;
                    boolean rightBool = (boolean) rightValue;

                    switch (operator) {
                        case GREATER_THAN:
                            result = leftBool && !rightBool;
                            break;
                        case LESS_THAN:
                            result = !leftBool && rightBool;
                            break;
                        default:
                            break;
                    }
                }

                values.set(i, result);
                values.remove(i + 1);
                operators.remove(i);
                break;
            }
        }
        return values.get(0);
    }

    private int executeVariableDeclaration(int index) {
        String variableName = tokens.get(index + 1).getLexeme();
        if (variables.containsKey(variableName)) {
            // La variable ya está definida
            System.out.println("Error: La variable '" + variableName + "' ya está definida");
        } else {
            // Avanzar al siguiente token después del '='
            index += 3;
            if (index < tokens.size()) {
                Object value = evaluateExpression(index);
                variables.put(variableName, value);
                return index;
            } else {
                // Error: Fin de tokens inesperado después de la declaración de variable
                System.out.println("Error: Fin de tokens inesperado después de la declaración de variable");
            }
        }
        return index;
    }

    private int executeReturnStatement(int index) {
        index++; // Avanzar al siguiente token después de "return"
        if (index < tokens.size()) {
            Token valueToken = tokens.get(index);
            switch (valueToken.getType()) {
                case IDENTIFIER:
                    String variableName = valueToken.getLexeme();
                    if (variables.containsKey(variableName)) {
                        Object value = variables.get(variableName);
                        results.add(value);
                        return tokens.size(); // Terminar la ejecución
                    } else {
                        System.out.println("Error: La variable '" + variableName + "' no está definida");
                    }
                    break;
                case NUMBER:
                    results.add(Integer.parseInt(valueToken.getLexeme()));
                    return tokens.size(); // Terminar la ejecución
                case BOOL_LITERAL:
                    results.add(Boolean.parseBoolean(valueToken.getLexeme()));
                    return tokens.size(); // Terminar la ejecución
                default:
                    System.out.println("Error: Tipo de valor no reconocido en la declaración de retorno");
                    break;
            }
        } else {
            System.out.println("Error: Fin de tokens inesperado después de la declaración de retorno");
        }
        return index;
    }

    private int executeIfStatement(int index) {
        Token conditionToken = tokens.get(index + 1);
        boolean condition = Boolean.parseBoolean(conditionToken.getLexeme());
        if (condition) {
            
            index = executeBlock(index + 3); // Asumiendo que la condición está seguida por '{'
        } else {
            // Encontrar el 'else' y ejecutar ese bloque, si existe
            int elseIndex = findElseIndex(index + 3);
            if (elseIndex != -1) {
                index = executeBlock(elseIndex + 1); // Asumiendo que 'else' está seguido por '{'
            }
        }
        return index;
    }

    private int findElseIndex(int start) {
        for (int i = start; i < tokens.size(); i++) {
            Token currentToken = tokens.get(i);
            if (currentToken.getType() == TokenType.ELSE) {
                return i;
            }
        }
        return -1; // 'else' no encontrado
    }

    private int executeBlock(int index) {
        int openBraces = 0;
        for (int i = index; i < tokens.size(); i++) {
            Token currentToken = tokens.get(i);
            if (currentToken.getType() == TokenType.LEFT_BRACE) {
                openBraces++;
            } else if (currentToken.getType() == TokenType.RIGHT_BRACE) {
                openBraces--;
                if (openBraces == 0) {
                    return i; // Fin del bloque
                }
            } else {
                i = executeStatement(i);
            }
        }
        return tokens.size();
    }
    private int executePrint(int index) {
        index++; // Avanzar al siguiente token después de "print"
        if (index < tokens.size()) {
            Token valueToken = tokens.get(index);
            Object value = null;
            switch (valueToken.getType()) {
                case IDENTIFIER:
                    String variableName = valueToken.getLexeme();
                    if (variables.containsKey(variableName)) {
                        value = variables.get(variableName);
                    } else {
                        System.out.println("Error: La variable `" + variableName + "` no está definida");
                    }
                    break;
                case NUMBER:
                    value = Integer.parseInt(valueToken.getLexeme());
                    break;
                case BOOL_LITERAL:
                    value = Boolean.parseBoolean(valueToken.getLexeme());
                    break;
                default:
                    // Otros casos según sea necesario
                    System.out.println("Error: Tipo de valor no reconocido en la declaración de impresión");
                    break;
            }
            if (value != null) {
                System.out.println(value);
            }
        } else {
            // Error: Fin de tokens inesperado después de la declaración de impresión
            System.out.println("Error: Fin de tokens inesperado después de la declaración de impresión");
        }
        return index;
    }
}
