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
            executeStatement(i);
        }

    }

    private void executeStatement(int index) {
        Token token = tokens.get(index);
        switch (token.getType()) {
            case VAR:
                executeVariableDeclaration(index);
                break;
            case FOR:
                /* executeForLoop(index); */
                break;
            case WHILE:
                /* executeWhileLoop(index); */
                break;
            case IF:
                executeIfStatement(index);
                break;
            case PRINT:
                executePrint(index);
                break;
            // Mostrar los resultados al final de la ejecución
            case RETURN:
                
                break;
            default:
                break;
        }
    }

    private Object executeVariableDeclaration(int index) {
        String variableName = tokens.get(index + 1).getLexeme();
        if (variables.containsKey(variableName)) {
            // La variable ya está definida
            System.out.println("Error: La variable '" + variableName + "' ya está definida");
        } else {
            // Avanzar al siguiente token después del '='
            index += 2;
            if (index < tokens.size()) {
                Token currentToken = tokens.get(index+1);
                if (currentToken.getType() == TokenType.STRING_LITERAL) {
                    // Si el token es una cadena literal, simplemente almacena su valor en la variable
                    variables.put(variableName, currentToken.getLexeme());
                    System.out.println("HEMOS ENTRADO");
                    return currentToken.getLexeme(); // Devolver el valor para actualizar la variable
                } else if (currentToken.getType() == TokenType.EQUALS) {
                    // Aquí es donde debes especificar el tipo de operación, en este caso, TokenType.EQUALS
                    index++; // Avanzar al siguiente token después del '='
                    if (index < tokens.size()) {
                        Object value = executeOperation(index, TokenType.EQUALS);
                        variables.put(variableName, value);
                        return value; // Devolver el valor para actualizar la variable
                    } else {
                        // Error: Fin de tokens inesperado después de la declaración de variable
                        System.out.println("Error: Fin de tokens inesperado después de la declaración de variable");
                    }
                } else {
                    // Error: Tipo de operando no reconocido
                    System.out.println("Error: Tipo de operando no reconocido en la declaración de variable");
                }
            } else {
                // Error: Fin de tokens inesperado después de la declaración de variable
                System.out.println("Error: Fin de tokens inesperado después de la declaración de variable");
            }
        }
        return null;
    }
    private Object executeOperation(int index, TokenType operationType) {
        List<Object> operands = new ArrayList<>();
        TokenType lastTokenType = null;
        boolean addNext = false;
        boolean subtractNext = false;
        boolean multiplyNext = false;
        boolean divideNext = false;
    
        while (index < tokens.size()) {
            Token currentToken = tokens.get(index);
    
            if (currentToken.getType() == TokenType.NUMBER || currentToken.getType() == TokenType.IDENTIFIER) {
                int operandValue = (int) getOperandValue(currentToken);
                if (addNext) {
                    operandValue += (int) operands.remove(operands.size() - 1);
                    addNext = false;
                } else if (subtractNext) {
                    operandValue *= -1;
                    operandValue += (int) operands.remove(operands.size() - 1);
                    subtractNext = false;
                } else if (multiplyNext) {
                    // Modificamos cómo se realiza la operación de multiplicación
                    int previousOperand = (int) operands.remove(operands.size() - 1);
                    operandValue = previousOperand * operandValue;
                    multiplyNext = false;
                } else if (divideNext) {
                    int previousOperand = (int) operands.remove(operands.size() - 1);
                    if (operandValue != 0) {
                        operandValue = previousOperand / operandValue;
                    } else {
                        System.out.println("Error: División por cero");
                        return 0; // O manejar el error de otra manera
                    }
                    divideNext = false;
                }
                operands.add(operandValue);
            } else if (currentToken.getType() == TokenType.ADD) {
                if (lastTokenType == TokenType.ADD) {
                    System.out.println("Error: Dos operadores de suma consecutivos");
                    return 0; // O manejar el error de otra manera
                }
                addNext = true;
            } else if (currentToken.getType() == TokenType.SUBSTRAIN) {
                if (lastTokenType == TokenType.SUBSTRAIN) {
                    System.out.println("Error: Dos operadores de resta consecutivos");
                    return 0; // O manejar el error de otra manera
                }
                subtractNext = true;
            } else if (currentToken.getType() == TokenType.MULTIPLY) {
                if (lastTokenType == TokenType.MULTIPLY || lastTokenType == TokenType.DIVIDE) {
                    System.out.println("Error: Dos operadores de multiplicación o división consecutivos");
                    return 0; // O manejar el error de otra manera
                }
                multiplyNext = true;
            } else if (currentToken.getType() == TokenType.DIVIDE) {
                if (lastTokenType == TokenType.MULTIPLY || lastTokenType == TokenType.DIVIDE) {
                    System.out.println("Error: Dos operadores de multiplicación o división consecutivos");
                    return 0; // O manejar el error de otra manera
                }
                divideNext = true;
            } else if (currentToken.getType() == TokenType.LEFT_PAREN) {
                // Avanzar al siguiente token después del '('
                int closingParenIndex = findClosingParenthesis(index);
                if (closingParenIndex != -1) {
                    // Calcular el valor dentro de los paréntesis recursivamente
                    int valueInsideParen = (int) executeOperation(index + 1, TokenType.LEFT_PAREN);
                    operands.add(valueInsideParen);
                    index = closingParenIndex + 1; // Avanzar al siguiente token después del ')'
                } else {
                    System.out.println("Error: Falta el paréntesis de cierre");
                    return 0; // O manejar el error de otra manera
                }
            } else if (currentToken.getType() == TokenType.RIGHT_PAREN) {
                // Fin de la expresión dentro de paréntesis
                break;
            } else if (currentToken.getType() == TokenType.SEMICOLON) {
                // Fin de la expresión
                break;
            }
    
            lastTokenType = currentToken.getType();
            index++; // Avanzar al siguiente token
        }
    
        // Calcular el resultado final
        Object result = calculateResult(operands);
        System.out.println("Debug:" + result);
        return result;
    }
    
    
    
    // Nueva función para encontrar el índice del paréntesis de cierre correspondiente
    private int findClosingParenthesis(int start) {
        int openParenCount = 0;
        for (int i = start + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.LEFT_PAREN) {
                openParenCount++;
            } else if (tokens.get(i).getType() == TokenType.RIGHT_PAREN) {
                if (openParenCount == 0) {
                    return i;
                }
                openParenCount--;
            }
        }
        System.out.println("Error: Falta el paréntesis de cierre correspondiente");
        return -1;
    }
    

    private Object getOperandValue(Token token) {
        int nextTokenIndex ;
        switch (token.getType()) {
            case NUMBER:
                return Integer.parseInt(token.getLexeme());
            case IDENTIFIER:
                String variableName = token.getLexeme();
                if (variables.containsKey(variableName)) {
                    return variables.get(variableName);
                } else {
                    System.out.println("Error: La variable '" + variableName + "' no esta definida");
                    return 0; // O manejar el error de otra manera
                }
            case LEFT_PAREN:
                // Avanzar al siguiente token después del '('
                nextTokenIndex = tokens.indexOf(token) + 1;
                return executeOperation(nextTokenIndex, TokenType.LEFT_PAREN);
            case RIGHT_PAREN:
                // Devolver el resultado de la expresión dentro de paréntesis
                nextTokenIndex = tokens.indexOf(token) + 1;
                return executeOperation(nextTokenIndex, TokenType.RIGHT_PAREN);
            default:
                System.out.println("Error: Tipo de operando no reconocido");
                return 0; // O manejar el error de otra manera
        }
    }

    
    

    private Object calculateResult(List<Object> operands) {
        int result = 0;
        for (Object operand : operands) {
            if (operand instanceof Integer) {
                result += (int) operand;
            } else {
                System.out.println("Error: Tipo de operando no admitido");
                return 0; // O manejar el error de otra manera
            }
        }
        return result;
    }

    private void executeReturnStatement(int index) {
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
                    } else {
                        System.out.println("Error: La variable '" + variableName + "' no está definida");
                    }
                    break;
                case NUMBER:
                    // Si el token es un número, simplemente agrega su valor a los resultados
                    results.add(Integer.parseInt(valueToken.getLexeme()));
                    break;
                case BOOL_LITERAL:
                    // Si el token es un número, simplemente agrega su valor a los resultados
                    results.add(Boolean.parseBoolean(valueToken.getLexeme()));
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

/*     private boolean isValidReturnContext(int index) {
        // Comprueba si el índice está dentro de un bloque 'if', 'for', o 'while'
        boolean isInValidContext = true;
        int depth = 0; // Profundidad del bloque
    
        // Recorre hacia atrás para verificar si está en un contexto válido
        for (int i = index; i >= 0; i--) {
            Token token = tokens.get(i);
            if (token.getType() == TokenType.LEFT_BRACE) {
                depth++;
            } else if (token.getType() == TokenType.RIGHT_BRACE) {
                depth--;
            }
    
            // Si está en un bloque condicional o de bucle, es válido
            if (depth == 1 && (token.getType() == TokenType.IF || token.getType() == TokenType.FOR || token.getType() == TokenType.WHILE)) {
                isInValidContext = false;
                break;
            }
        }
    
        return isInValidContext;
    } */

    private void executePrint(int index) {
        index += 2; // Avanzar al siguiente token después de '('

        if (index < tokens.size()) {
            Token variableToken = tokens.get(index);
            String variableName = variableToken.getLexeme();
            if (variables.containsKey(variableName)) {
                Object value = variables.get(variableName);
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
    private void executeIfStatement(int index) {
        index += 2; // Avanzar después de '('
    
        Token leftOperandToken = tokens.get(index);
        Token operatorToken = tokens.get(index + 1);
        Token rightOperandToken = tokens.get(index + 2);
    
        String leftOperand = leftOperandToken.getLexeme();
        String operator = operatorToken.getLexeme();
        String rightOperand = rightOperandToken.getLexeme();
    
        Object leftValue = variables.get(leftOperand);
        Object rightValue = variables.get(rightOperand);
    
        boolean condition = false;
    
        switch (operator) {
            case ">":
                condition = (Integer) leftValue > (Integer) rightValue;
                break;
            case "<":
                condition = (Integer) leftValue < (Integer) rightValue;
                break;
            case "=":
                condition = leftValue.equals(rightValue);
                break;
        }
    
        if (condition) {
            executeBlock(index + 3); // Ejecutar el bloque `if`
        } else {
            if (tokens.get(index + 3).getType() == TokenType.ELSE) {
                executeBlock(index + 4); // Ejecutar el bloque `else`
            }
        }
    }

    private void executeBlock(int start) {
        int braceCount = 1; // Hemos encontrado la primera llave de apertura
        int i = start + 1;

        while (i < tokens.size()) {
            Token currentToken = tokens.get(i);

            if (currentToken.getType() == TokenType.LEFT_BRACE) {
                braceCount++; // Incrementar cuando se encuentra una llave de apertura
            } else if (currentToken.getType() == TokenType.RIGHT_BRACE) {
                braceCount--; // Decrementar cuando se encuentra una llave de cierre

                if (braceCount == 0) {
                    break; // Terminar el bloque cuando se cierran todas las llaves
                }
            }else if(currentToken.getType() == TokenType.RETURN)
            {
                executeReturnStatement(i);
            }

            executeStatement(i); // Ejecutar cada declaración dentro del bloque
            i++;
        }
    }
}