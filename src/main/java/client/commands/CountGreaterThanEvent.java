package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The 'CountGreaterThanEvent' class represents a command that counts the number of tickets whose associated event has a ticket count greater than a given value.
 *
 * Attributes:
 * - client: Client - The client object that contains the list of tickets.
 *
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(Client client): void - Sets the client object.
 * - execute(String value): String - Executes the command and returns the result as a string.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class CountGreaterThanEvent implements Command {
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
        int ticketsCount;
        try {
            ticketsCount = Integer.parseInt(value);
            ArrayList<Object> params = new ArrayList<>();
            params.add(ticketsCount);
            return new Request(getName(), getValue(), params);
        } catch (NumberFormatException ignored) {
            throw new CommandValueException("int");
        }

    }

    @Override
    public String getName() {
        return "count_greater_than_event";
    }

    @Override
    public String description() {
        return "<int> вывести количество элементов, значение поля event(tickets count) которых больше заданного";
    }
}
