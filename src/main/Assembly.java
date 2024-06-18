package main;

import java.util.List;

public class Assembly {
    public static void generateAssembly(List<Token> tokens) {
        StringBuilder assemblyCode = new StringBuilder();
        System.out.println("------------  EJEMPLO CODIGO EN ENSAMBLADOR ------------");
        // Encabezado del código ensamblador
        assemblyCode.append("section .text\n")
                    .append("global _start\n")
                    .append("_start:\n");
        
        // Recorrer los tokens para generar el código ensamblador
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            switch (token.getType()) {
                case VAR:
                    // Generar código ensamblador para la declaración de variable
                    String variableName = tokens.get(i + 1).getLexeme();
                    String value = tokens.get(i + 3).getLexeme();
                    assemblyCode.append("mov eax, ").append(value).append("\n")
                                .append("mov ").append(variableName).append(", eax\n");
                    i += 4; // Avanzar al siguiente token después de la declaración de variable
                    break;
                case ADD:
                    // Generar código ensamblador para la operación de suma
                    String operand1Add = tokens.get(i - 1).getLexeme();
                    String operand2Add = tokens.get(i + 1).getLexeme();
                    assemblyCode.append("mov eax, ").append(operand1Add).append("\n")
                                .append("add eax, ").append(operand2Add).append("\n");
                    i += 2; // Avanzar al siguiente token después de la operación de suma
                    break;
                case SUBSTRAIN:
                    // Generar código ensamblador para la operación de resta
                    String operand1Sub = tokens.get(i - 1).getLexeme();
                    String operand2Sub = tokens.get(i + 1).getLexeme();
                    assemblyCode.append("mov eax, ").append(operand1Sub).append("\n")
                                .append("sub eax, ").append(operand2Sub).append("\n");
                    i += 2; // Avanzar al siguiente token después de la operación de resta
                    break;
                case MULTIPLY:
                    // Generar código ensamblador para la operación de multiplicación
                    String operand1Mul = tokens.get(i - 1).getLexeme();
                    String operand2Mul = tokens.get(i + 1).getLexeme();
                    assemblyCode.append("mov eax, ").append(operand1Mul).append("\n")
                                .append("imul ").append(operand2Mul).append("\n");
                    i += 2; // Avanzar al siguiente token después de la operación de multiplicación
                    break;
                case DIVIDE:
                    // Generar código ensamblador para la operación de división
                    String operand1Div = tokens.get(i - 1).getLexeme();
                    String operand2Div = tokens.get(i + 1).getLexeme();
                    assemblyCode.append("mov eax, ").append(operand1Div).append("\n")
                                .append("cdq\n") // Signo extendido para dividir números con signo
                                .append("idiv ").append(operand2Div).append("\n");
                    i += 2; // Avanzar al siguiente token después de la operación de división
                    break;
                case RETURN:
                    // Generar código ensamblador para la declaración de retorno
                    String returnValue = tokens.get(i + 1).getLexeme();
                    assemblyCode.append("mov eax, ").append(returnValue).append("\n");
                    // Avanzar al siguiente token después de la declaración de retorno
                    i += 2;
                    break;
                case PRINT:
                    // Generar código ensamblador para la declaración de impresión
                    String printValue = tokens.get(i + 2).getLexeme();
                    assemblyCode.append("mov eax, ").append(printValue).append("\n")
                                .append("call print_int\n"); // Suponiendo que hay una función de impresión llamada print_int
                    // Avanzar al siguiente token después de la declaración de impresión
                    i += 2;
                    break;
                case IF:
                    // Generar código ensamblador para la declaración de if
                    String condition = tokens.get(i + 2).getLexeme();
                    String labelIf = "label_" + i; // Crear una etiqueta única para el bloque 'if'
                    assemblyCode.append("cmp ").append(condition).append(", 0\n")
                                .append("je ").append(labelIf).append("\n");
                    // Avanzar al siguiente token después de la declaración de if
                    i += 2;
                    break;
                default:
                    // Manejar otros casos si es necesario
                    break;
            }
        }
        
        // Llamada a la salida del programa
        assemblyCode.append("call exit\n------------  EJEMPLO CODIGO EN ENSAMBLADOR ------------");
        System.out.println(assemblyCode.toString());
    }
}