package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The Shuffle class represents a command that shuffles the elements of a collection in random order.
 * It implements the Command interface and provides functionality for executing the shuffle command.
 * The shuffle command requires a non-empty collection to be present in the client's ListManager.
 * If the collection is empty, a CommandCollectionZeroException is thrown.
 *
 * Usage:
 * Shuffle shuffleCommand = new Shuffle();
 * shuffleCommand.setServer(client); // Set the client for the command
 * String result = shuffleCommand.execute(""); // Execute the shuffle command
 *
 * Example:
 * Shuffle shuffleCommand = new Shuffle();
 * shuffleCommand.setServer(client);
 * String result = shuffleCommand.execute("");
 *
 * Command Name: shuffle
 * Description: перемешать элементы коллекции в случайном порядке
 */
public class Shuffle implements Command {
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
        return "shuffle";
    }

    @Override
    public String description() {
        return "<> перемешать элементы коллекции в случайном порядке";
    }
}
