package server.commands;

import commons.exceptions.BadRequestException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The 'CountGreaterThanEvent' class represents a command that counts the number of tickets whose associated event has a ticket count greater than a given value.
 *
 * Attributes:
 * - server: Server - The server object that contains the list of tickets.
 *
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(Server server): void - Sets the server object.
 * - execute(String value): String - Executes the command and returns the result as a string.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class CountGreaterThanEvent implements Command {
    private Server server;

    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        if(params.get(0) instanceof Integer){
            if (server.getListManager().getTicketList().isEmpty()) {
                throw new CommandCollectionZeroException("collection is zero");
            }
            int value = (int) params.get(0);
            long count = server.getListManager().getTicketList().stream()
                    .filter(ticket -> ticket.getEvent() != null && ticket.getEvent().getTicketsCount() > value)
                    .count();
            return new Response(getName(), "Count events greater than " + value + " by ticket count: " + count + "\n");
        }
        throw new BadRequestException("need an Integer");
    }

    @Override
    public String getName() {
        return "count_greater_than_event";
    }

    @Override
    public String description() {
        return "вывести количество элементов, значение поля event(tickets count) которых больше заданного";
    }
}
