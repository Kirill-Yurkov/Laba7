package commons.exceptions;
/**
 * The StopCreateTicketExceptionByClient class is a custom exception that is thrown when the creation of a ticket needs to be stopped.
 * This exception can be used to handle specific scenarios where the creation of a ticket should not proceed.
 * It extends the Exception class, making it a checked exception that needs to be caught or declared in a method's throws clause.
 *
 * Example usage:
 * try {
 *     // code that may throw StopCreateTicketExceptionByClient
 * } catch (StopCreateTicketExceptionByClient e) {
 *     // handle the exception
 * }
 *
 * @since 1.0
 */
public class StopCreateTicketExceptionByClient extends Exception{

}
