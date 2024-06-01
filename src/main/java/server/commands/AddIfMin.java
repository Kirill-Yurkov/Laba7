package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;

import java.util.ArrayList;

/**
 * The 'AddIfMin' class represents a command that adds a new element to the collection if its value is smaller than the value of the smallest element in the collection.
 * <p>
 * Attributes:
 * - server: Server (required) - The server object that contains the list manager and ticket creator.
 * <p>
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(Server server): void - Sets the server object.
 * - execute(String value): String - Executes the command by creating a new ticket and checking if its price is smaller than the smallest price in the collection. Returns "successfully" if the ticket is added, throws CommandValueException if the price is greater than the smallest price, and throws CommandCollectionZeroException if the collection is empty.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class AddIfMin implements Command {
    private Server server;

    @Override
    public CommandValues getValue() {
        return CommandValues.ELEMENT;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        if (server.getListManager().getTicketList().isEmpty()) {
            throw new CommandCollectionZeroException("collection is zero");
        }
        if(params.get(0) instanceof Ticket){
            Ticket ticket = (Ticket) params.get(0);
            int mini = server.getListManager().getTicketList().stream()
                    .filter(localTicket -> localTicket.getPrice() != null)
                    .mapToInt(Ticket::getPrice)
                    .min()
                    .orElse(Integer.MAX_VALUE);
            if (ticket.getPrice() < mini) {
                ticket.setId(server.getIdCounter().getIdForTicket(ticket));
                if (ticket.getEvent() != null) {
                    ticket.getEvent().setId(server.getIdCounter().getIdForEvent(ticket.getEvent()));
                }
                return new Response(getName(), "successfully added");
            }
            throw new CommandValueException("price more than minimal");
        }
        throw new BadRequestException("need a Ticket");
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String description() {
        return "добавить новый элемент в коллекцию, если его значение меньше (price), чем у наименьшего элемента этой коллекции";
    }
}
