package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.patternclass.TicketType;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.util.ArrayList;

/**
 * The FilterLessThanType class represents a command that filters and returns the elements from the ticket list
 * whose type has a lower priority than the specified value.
 *
 * Usage:
 * FilterLessThanType filter = new FilterLessThanType();
 * filter.setServer(client); // Set the client for the command
 * String result = filter.execute(value); // Execute the command with the specified value
 *
 * Example:
 * FilterLessThanType filter = new FilterLessThanType();
 * filter.setServer(client);
 * String result = filter.execute("VIP");
 *
 * Command Name: filter_less_than_type
 * Description: выводит элементы, значение поля type (VIP>USUAL>CHEAP) которых меньше заданного (value)
 */
public class FilterLessThanType implements Command {
    private Client client;
    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE;
    }

    @Override
    public void setClient(Client client) {
        this.client=client;
    }

    @Override
    public Request makeRequest(String value) throws CommandValueException, CommandCollectionZeroException {
        TicketType type;
        try {
            type = TicketType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new CommandValueException("type");
        }
        ArrayList<Object> params = new ArrayList<>();
        params.add(type);
        return new Request(getName(), getValue(), params);
    }

    @Override
    public String getName() {
        return "filter_less_than_type";
    }

    @Override
    public String description() {
        return "<TicketType> вывести элементы, значение поля type (VIP>USUAL>CHEAP) которых меньше заданного";
    }
}
