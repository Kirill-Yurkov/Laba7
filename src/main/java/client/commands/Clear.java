package client.commands;

import client.Client;
import client.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;
import commons.requests.RequestOfCommand;

import java.util.ArrayList;

/**
 * The Clear class represents a command that clears the ticket collection.
 * It implements the Command interface and provides functionality for executing the command.
 *
 * Usage:
 * Clear clearCommand = new Clear();
 * clearCommand.setServer(server); // Set the server for the command
 * String result = clearCommand.execute(value); // Execute the command with the given value
 * String name = clearCommand.getName(); // Get the name of the command
 * String description = clearCommand.description(); // Get the description of the command
 *
 * Example:
 * Clear clearCommand = new Clear();
 * clearCommand.setServer(server);
 * String result = clearCommand.execute("");
 * String name = clearCommand.getName();
 * String description = clearCommand.description();
 *
 */
public class Clear implements Command {
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
        return new RequestOfCommand(getName(), getValue(), new ArrayList<>(),client.getLogin(), client.getPassword() );
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String description() {
        return "<> очистить коллекцию";
    }
}
