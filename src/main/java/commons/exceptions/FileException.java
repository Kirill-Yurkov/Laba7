package commons.exceptions;
/**
 * The FileException class is a custom exception class that is used to handle exceptions related to file operations.
 * It extends the Exception class and provides a constructor to set the error message.
 *
 */
public class FileException extends Exception{
    public FileException(String message){
        super(message);
    }
}
