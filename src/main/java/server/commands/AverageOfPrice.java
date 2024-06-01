package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandValueException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The AverageOfPrice class represents a command that calculates the average value of the 'price' field for all elements in the collection.
 * It implements the Command interface and provides methods for executing the command, setting the server, getting the command value, getting the command name, and getting the command description.
 *
 * Usage:
 * AverageOfPrice averageOfPrice = new AverageOfPrice();
 * averageOfPrice.setServer(server); // Set the server
 * String result = averageOfPrice.execute(value); // Execute the command and get the result
 * String name = averageOfPrice.getName(); // Get the command name
 * String description = averageOfPrice.description(); // Get the command description
 *
 * Example:
 * AverageOfPrice averageOfPrice = new AverageOfPrice();
 * averageOfPrice.setServer(server);
 * String result = averageOfPrice.execute(value);
 * String name = averageOfPrice.getName();
 * String description = averageOfPrice.description();
 */
public class AverageOfPrice implements Command {
    private Server server;

    @Override
    public CommandValues getValue() {
        return CommandValues.NOTHING;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        int price = server.getListManager().getTicketList().stream()
                .filter(ticket -> ticket.getPrice() != null)
                .mapToInt(Ticket::getPrice)
                .sum();
        if (server.getListManager().getTicketList().isEmpty()) {
            throw new CommandCollectionZeroException("collection is zero");
        } else {
            return new Response(getName(),String.valueOf(price / server.getListManager().getTicketList().size()));
        }
    }

    @Override
    public String getName() {
        return "average_of_price";
    }

    @Override
    public String description() {
        return "вывести среднее значение поля price для всех элементов коллекции";
    }
}
