package server.commands;

import commons.exceptions.BadRequestException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;

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
    public Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        if((params.get(0) instanceof Ticket && params.get(1) instanceof Long)||(params.get(1) instanceof Ticket && params.get(0) instanceof Long)){
            if(params.get(0) instanceof Ticket){
                long id = (long) params.get(1);
                if(server.getListManager().getTicketList().isEmpty()){
                    throw new CommandCollectionZeroException("collection is zero");
                }
                for(Ticket ticket: server.getListManager().getTicketList()){
                    if (ticket.getId() == id){
                        server.getListManager().remove(ticket);
                        Ticket newTicket = (Ticket) params.get(0);
                        newTicket.setId(id);
                        if(newTicket.getEvent()!=null){
                            newTicket.getEvent().setId(server.getIdCounter().getIdForEvent(newTicket.getEvent()));
                        }
                        server.getListManager().add(newTicket);
                        return new Response(getName(), "successfully");
                    }
                }
                throw new CommandValueException("id not find");
            } else{
                long id = (long) params.get(0);
                if(server.getListManager().getTicketList().isEmpty()){
                    throw new CommandCollectionZeroException("collection is zero");
                }
                for(Ticket ticket: server.getListManager().getTicketList()){
                    if (ticket.getId() == id){
                        server.getListManager().remove(ticket);
                        Ticket newTicket = (Ticket) params.get(1);
                        newTicket.setId(id);
                        if(newTicket.getEvent()!=null){
                            newTicket.getEvent().setId(server.getIdCounter().getIdForEvent(newTicket.getEvent()));
                        }
                        server.getListManager().add(newTicket);
                        return new Response(getName(), "successfully");
                    }
                }
                throw new CommandValueException("id not find");
            }
        }
        throw new BadRequestException("need a Ticket and Long");
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
