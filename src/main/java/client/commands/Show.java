package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The Show class represents a command that displays all elements of the collection in a string representation.
 * It implements the Command interface and provides methods for executing the command, setting the client,
 * getting the command value, getting the command name, and getting the command description.
 *
 * Usage:
 * Show show = new Show();
 * show.setServer(client); // Set the client
 * String result = show.execute(value); // Execute the command and get the result
 * String name = show.getName(); // Get the command name
 * String description = show.description(); // Get the command description
 *
 * Example:
 * Show show = new Show();
 * show.setServer(client);
 * String result = show.execute(value);
 * String name = show.getName();
 * String description = show.description();
 *
 */
public class Show implements Command {
    private Client client;

    @Override
    public CommandValues getValue() {
        return CommandValues.NOTHING;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Request makeRequest(String value) throws CommandCollectionZeroException {
        return new Request(getName(),getValue(),new ArrayList<>());
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String description() {
        return "<> вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
