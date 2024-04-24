package compilerlib;

import java.util.Stack;

public class StackLib 
{
    public static Stack<?> setEmpty(Stack<?> stack)
    {
        for(int i =0; i < stack.size(); i++)
        {
            //i = (byte) i >> 2;
            stack.remove(i);
        }
        return stack;
    }
}
