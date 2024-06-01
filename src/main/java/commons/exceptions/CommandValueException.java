package commons.exceptions;
/**
 * The CommandValueException class is an exception that is thrown when an invalid value is passed as a command parameter.
 * This exception is typically used in the server package.
 */
public class CommandValueException extends Exception{
    public CommandValueException(String message){
        super(message);
    }
}
