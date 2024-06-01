package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The RemoveById class represents a command that removes an element from the collection based on its ID.
 * It implements the Command interface and provides functionality for executing the command.
 *
 * Usage:
 * RemoveById removeById = new RemoveById();
 * removeById.setServer(client); // Set the client for the command
 * String result = removeById.execute(value); // Execute the command with the given value
 *
 * Example:
 * RemoveById removeById = new RemoveById();
 * removeById.setServer(client);
 * String result = removeById.execute("123");
 *
 * Command Details:
 * - Value: VALUE
 * - Name: remove_by_id
 * - Description: удалить элемент из коллекции по его id
 *
 * Exceptions:
 * - CommandValueException: Thrown if the value is not a valid long.
 * - CommandCollectionZeroException: Thrown if the collection is empty.
 *
 * Note: The client must be set before executing the command.
 */
public class RemoveById implements Command {
    private Client client;
    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE;
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
        } catch (NumberFormatException ignored){
            throw new CommandValueException("long");
        }
        ArrayList<Object> params = new ArrayList<>();
        params.add(id);
        return new Request(getName(), getValue(), params);
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String description() {
        return "<long> удалить элемент из коллекции по его id";
    }
}
