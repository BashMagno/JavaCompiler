package compilerlib;

import java.util.Stack;

public class StackLib 
{
    public static Stack<Integer> setEmpty(Stack<Integer> stack)
    {
        for(int i =0; i < stack.size(); i++)
        {
            stack.remove(i);
        }
        return stack;
    }
}
