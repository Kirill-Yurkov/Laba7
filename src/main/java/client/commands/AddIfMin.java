package client.commands;

import client.Client;
import client.commands.interfaces.Command;
import commons.exceptions.*;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.utilities.CommandValues;
import commons.patternclass.Ticket;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The 'AddIfMin' class represents a command that adds a new element to the collection if its value is smaller than the value of the smallest element in the collection.
 *
 * Attributes:
 * - client: client (required) - The client object that contains the list manager and ticket creator.
 *
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(client client): void - Sets the client object.
 * - execute(String value): String - Executes the command by creating a new ticket and checking if its price is smaller than the smallest price in the collection. Returns "successfully" if the ticket is added, throws CommandValueException if the price is greater than the smallest price, and throws CommandCollectionZeroException if the collection is empty.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class AddIfMin implements Command {
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
    public Request makeRequest(String value) {
        Ticket ticket;
        try {
            if (client.isWithFile()) {
                ticket = client.getTicketCreator().createTicketGroup(true);
            } else {
                ticket = client.getTicketCreator().createTicketGroup();
            }
            ArrayList<Object> params = new ArrayList<>();
            params.add(ticket);
            return new Request(getName(), getValue(), params);
        } catch (StopCreateTicketExceptionByClient e) {
            return null;
        }
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String description() {
        return "<Ticket> добавить новый элемент в коллекцию, если его значение меньше (price), чем у наименьшего элемента этой коллекции";
    }
}
