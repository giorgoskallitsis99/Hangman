package exceptions;

public class UnbalancedException extends Exception{

    public UnbalancedException(){
        //This exception occurs when the percentage of words with at least 9 letters in the candidate dictionary is less than 20%.
        super("The dictionary is not balanced.");
    }
}