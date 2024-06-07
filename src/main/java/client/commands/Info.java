package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.requests.RequestOfCommand;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The Info class implements the Command interface and represents a command to display information about the collection.
 * It provides methods to get the command value, set the client, execute the command, get the command name, and get the command description.
 *
 * The execute method retrieves the collection information from the client's reader writer and returns it as a string.
 * If the collection information is empty, it throws a CommandCollectionZeroException.
 *
 * The getName method returns the name of the command as "info".
 *
 * The description method returns a description of the command as "вывести в стандартный поток вывода информацию о коллекции".
 */
public class Info implements Command {
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
    public RequestOfCommand makeRequest(String value) throws CommandCollectionZeroException {
        return new RequestOfCommand(getName(),getValue(),new ArrayList<>(), client.getLogin(), client.getPassword());
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String description() {
        return "<> output information about the collection to the standard output stream";
    }
}
