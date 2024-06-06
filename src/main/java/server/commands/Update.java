package server.commands;

import commons.exceptions.BadRequestException;
import commons.responses.ResponseOfCommand;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The 'Update' class represents a command that updates the value of an element in the collection
 * with the specified ID.
 *
 * Attributes:
 * - server: Server - The server object that contains the collection and other necessary components.
 *
 * Methods:
 * - getValue(): CommandValues - Returns the value of the command.
 * - setServer(Server server): void - Sets the server object.
 * - execute(String value): String - Executes the command with the specified value.
 * - getName(): String - Returns the name of the command.
 * - description(): String - Returns the description of the command.
 */
public class Update implements Command {
    private Server server;
    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE_ELEMENT;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public ResponseOfCommand makeResponse(ArrayList<Object> params, int userId) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        try {
            if((params.get(0) instanceof Ticket && params.get(1) instanceof Long)||(params.get(1) instanceof Ticket && params.get(0) instanceof Long)){
                long id;
                Ticket newTicket;
                if(params.get(0) instanceof Ticket){
                    newTicket = (Ticket) params.get(0);
                    id = (long) params.get(1);
                } else{
                    id = (long) params.get(0);
                    newTicket = (Ticket) params.get(1);
                }
                if(server.getListManager().getTicketListOfUser(userId).isEmpty()){
                    throw new CommandCollectionZeroException("collection is zero");
                }
                for(Ticket ticket: server.getListManager().getTicketListOfUser(userId)){
                    if (ticket.getId() == id){
                        server.getListManager().update(newTicket, id);
                        return new ResponseOfCommand(getName(), "successfully");
                    }
                }
                throw new CommandValueException("id not find");
            }
            throw new BadRequestException("need a Ticket and Long");
        } catch (SQLException e){
            throw new BadRequestException("error on data base");
        }

    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String description() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
