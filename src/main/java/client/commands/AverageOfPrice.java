package client.commands;

import client.Client;
import client.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The AverageOfPrice class represents a command that calculates the average value of the 'price' field for all elements in the collection.
 * It implements the Command interface and provides methods for executing the command, setting the client, getting the command value, getting the command name, and getting the command description.
 * <p>
 * Usage:
 * AverageOfPrice averageOfPrice = new AverageOfPrice();
 * averageOfPrice.setServer(client); // Set the client
 * String result = averageOfPrice.execute(value); // Execute the command and get the result
 * String name = averageOfPrice.getName(); // Get the command name
 * String description = averageOfPrice.description(); // Get the command description
 * <p>
 * Example:
 * AverageOfPrice averageOfPrice = new AverageOfPrice();
 * averageOfPrice.setServer(client);
 * String result = averageOfPrice.execute(value);
 * String name = averageOfPrice.getName();
 * String description = averageOfPrice.description();
 */
public class AverageOfPrice implements Command {
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
        return new Request(getName(), getValue(), new ArrayList<>());
    }

    @Override
    public String getName() {
        return "average_of_price";
    }

    @Override
    public String description() {
        return "<> вывести среднее значение поля price для всех элементов коллекции";
    }
}
