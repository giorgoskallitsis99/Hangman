package exceptions;

public class UndersizeException extends Exception{

    public UndersizeException(){
        //This exception occurs when the candidate dictionary has not at least 20 available words.
        super("This dictionary has not 20+ words");
    }
}
