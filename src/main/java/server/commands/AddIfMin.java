package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.patternclass.Ticket;
import commons.responses.ResponseOfCommand;
import commons.utilities.CommandValues;
import server.Server;
import server.commands.interfaces.Command;

import java.sql.SQLException;
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
    public ResponseOfCommand makeResponse(ArrayList<Object> params, int userId) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        try {
            if (server.getListManager().getTicketListOfUser(userId).isEmpty()) {
                throw new CommandCollectionZeroException("YOUR collection is zero");
            }
            if (params.get(0) instanceof Ticket ticket) {
                int mini = server.getListManager().getTicketListOfUser(userId).stream()
                        .filter(localTicket -> localTicket.getPrice() != null)
                        .mapToInt(Ticket::getPrice)
                        .min()
                        .orElse(Integer.MAX_VALUE);
                if (ticket.getPrice() < mini) {
                    server.getListManager().add(ticket, userId);
                    return new ResponseOfCommand(getName(), "successfully added");
                }
                throw new CommandValueException("price more than minimal");
            }
        }catch (SQLException e){
            throw new BadRequestException("error on data base");
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
