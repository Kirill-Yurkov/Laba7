package commons.exceptions;
/**
 * The CommandCollectionZeroException class is an exception that is thrown when a command collection is empty.
 * This exception is typically thrown when attempting to perform an operation on an empty command collection.
 * The exception message can be customized to provide more specific information about the error.
 */
public class CommandCollectionZeroException extends Exception{
    public CommandCollectionZeroException(String message){
        super(message);
    }
}
