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
import java.util.List;
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
        List<Ticket> tickets = new ArrayList<>();
        if (server.getListManager().getTicketList().isEmpty()) {
            throw new CommandCollectionZeroException("collection is empty");
        }
        server.getListManager().setTicketList(tickets);
        return new Response(getName(), "successfully cleaned");
    }


    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String description() {
        return "очистить коллекцию";
    }
}
