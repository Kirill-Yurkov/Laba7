package commons.exceptions;
/**
 * The ServerMainResponseException class is a custom exception that is thrown when there is a request to stop the server.
 * It extends the Exception class and provides a constructor to set the exception message.
 */

public class ServerMainResponseException extends Exception{
    public ServerMainResponseException(String message){
        super(message);
    }
}
