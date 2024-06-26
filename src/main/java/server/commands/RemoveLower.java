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
import java.util.List;

/**
 * The RemoveLower class is a command implementation that removes all elements from the collection
 * that have a price lower than the specified ticket's price.
 * It implements the Command interface and provides methods to execute the command, set the server,
 * get the command value, get the command name, and get the command description.
 * <p>
 * Usage:
 * RemoveLower removeLower = new RemoveLower();
 * removeLower.setServer(server); // Set the server for the command
 * String result = removeLower.execute(value); // Execute the command with the specified value
 * String name = removeLower.getName(); // Get the name of the command
 * String description = removeLower.description(); // Get the description of the command
 * <p>
 * Example:
 * RemoveLower removeLower = new RemoveLower();
 * removeLower.setServer(server);
 * String result = removeLower.execute("100");
 * String name = removeLower.getName();
 * String description = removeLower.description();
 * <p>
 * The RemoveLower command requires a server instance to be set before execution.
 * It creates a new ticket using the TicketCreator and compares the price of each ticket in the collection
 * with the price of the new ticket. If the ticket's price is lower, it is added to a removeList.
 * After iterating through all tickets, the removeList is used to remove the tickets from the collection.
 * The execute method returns "successfully" if the operation is completed without any exceptions.
 * If a StopCreateTicketExceptionByClient is caught during ticket creation, the execute method returns null.
 * The getName method returns the name of the command ("remove_lower").
 * The description method returns a description of the command ("удалить из коллекции все элементы, меньшие, чем заданный(по price)").
 */
public class RemoveLower implements Command {
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
            if(params.get(0) instanceof Ticket newTicket) {
                List<Ticket> removeList = new ArrayList<>();
                if (server.getListManager().getTicketListOfUser(userId).isEmpty()) {
                    throw new CommandCollectionZeroException("YOUR collection is zero");
                }
                for (Ticket ticket : server.getListManager().getTicketListOfUser(userId)) {
                    if (ticket.getPrice() < newTicket.getPrice()) {
                        removeList.add(ticket);
                    }
                }
                for (Ticket ticket : removeList) {
                    server.getListManager().getTicketListOfUser(userId).remove(ticket);
                }
                return new ResponseOfCommand(getName(), "successfully");
            }
            throw new BadRequestException("need a Ticket");
        } catch (SQLException e){
            throw new BadRequestException("error on data base");
        }

    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String description() {
        return "remove all tickets from the collection that are smaller than the specified one (by price)";
    }
}
