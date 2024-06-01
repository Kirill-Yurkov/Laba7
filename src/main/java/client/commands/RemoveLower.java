package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The RemoveLower class is a command implementation that removes all elements from the collection
 * that have a price lower than the specified ticket's price.
 * It implements the Command interface and provides methods to execute the command, set the client,
 * get the command value, get the command name, and get the command description.
 *
 * Usage:
 * RemoveLower removeLower = new RemoveLower();
 * removeLower.setServer(client); // Set the client for the command
 * String result = removeLower.execute(value); // Execute the command with the specified value
 * String name = removeLower.getName(); // Get the name of the command
 * String description = removeLower.description(); // Get the description of the command
 *
 * Example:
 * RemoveLower removeLower = new RemoveLower();
 * removeLower.setServer(client);
 * String result = removeLower.execute("100");
 * String name = removeLower.getName();
 * String description = removeLower.description();
 *
 * The RemoveLower command requires a client instance to be set before execution.
 * It creates a new ticket using the TicketCreator and compares the price of each ticket in the collection
 * with the price of the new ticket. If the ticket's price is lower, it is added to a removeList.
 * After iterating through all tickets, the removeList is used to remove the tickets from the collection.
 * The execute method returns "successfully" if the operation is completed without any exceptions.
 * If a StopCreateTicketExceptionByClient is caught during ticket creation, the execute method returns null.
 * The getName method returns the name of the command ("remove_lower").
 * The description method returns a description of the command ("удалить из коллекции все элементы, меньшие, чем заданный(по price)").
 */
public class RemoveLower implements Command {
    private Client client;
    @Override
    public CommandValues getValue() {
        return CommandValues.ELEMENT;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Request makeRequest(String value) throws CommandCollectionZeroException {
        try {
           Ticket ticket = client.getTicketCreator().createTicketGroup();
            ArrayList<Object> params = new ArrayList<>();
            params.add(ticket);
            return new Request(getName(), getValue(), params);
        } catch (StopCreateTicketExceptionByClient e) {
            return null;
        }

    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String description() {
        return "<Ticket> удалить из коллекции все элементы, меньшие, чем заданный(по price)";
    }
}
