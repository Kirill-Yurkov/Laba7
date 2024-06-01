package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The 'Update' class represents a command that updates the value of an element in the collection
 * with the specified ID.
 *
 * Attributes:
 * - client: Client - The client object that contains the collection and other necessary components.
 *
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(Client client): void - Sets the client object.
 * - execute(String value): String - Executes the command with the specified value.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class Update implements Command {
    private Client client;
    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE_ELEMENT;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Request makeRequest(String value) throws CommandValueException, CommandCollectionZeroException {
        long id;
        try {
            id = Long.parseLong(value);
            Ticket ticket = client.getTicketCreator().createTicketGroup();
            ArrayList<Object> params = new ArrayList<>();
            params.add(ticket);
            return new Request(getName(), getValue(), params);
        } catch (NumberFormatException ignored){
            throw new CommandValueException("long");
        } catch (StopCreateTicketExceptionByClient e) {
            return null;
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String description() {
        return "<long (Ticket)> обновить значение элемента коллекции, id которого равен заданному";
    }
}
