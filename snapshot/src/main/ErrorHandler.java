package main;

public enum ErrorHandler 
{

    ERROR_PREFIX("AHM - PROGRAMMING LANGUAGE - By Alejandro Higuera Moreno - "),

    ERROR_ONE("ERROR CODE [1]: Usage: $> java Compiler <file.ahm>"),

    ERROR_TWO("ERROR CODE [2]: The file you provided does not match the extension '.ahm' "),

    ERROR_THREE("ERROR CODE [3]: Variable type not found.");

    

    private final String errorMessage;

    

    ErrorHandler(String errorMessage) 
    {
        this.errorMessage = errorMessage;
    }

    public static void getError(ErrorHandler error) 
    {
        System.out.println(error.getErrorMessage());
    }

    public String getErrorMessage() 
    {
        return errorMessage;
    }

    
}
