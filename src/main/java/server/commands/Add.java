package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.patternclass.Ticket;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The 'Add' class is responsible for adding a new element to the collection.
 * It implements the 'Command' interface and provides the necessary methods to execute the command.
 * The 'Add' command takes a value as input and creates a new 'Ticket' object based on the provided value.
 * The created ticket is then added to the collection using the 'ListManager' class.
 * If the server is running with a file, the ticket is created with additional information and assigned unique IDs using the 'IdCounter' class.
 * The 'Add' command returns a success message if the ticket is successfully created and added to the collection.
 * If there is an exception during the ticket creation process, the command returns null.
 *
 * The 'Add' class has the following methods:
 * - getValue(): Returns the value of the command, which is 'ELEMENT' in this case.
 * - setServer(Server server): Sets the server instance for the command.
 * - execute(String value): Executes the 'Add' command by creating a new ticket and adding it to the collection.
 *                          Returns a success message if the ticket is added successfully, or null if there is an exception.
 * - getName(): Returns the name of the command, which is 'add' in this case.
 * - description(): Returns a description of the command, which is "добавить новый элемент в коллекцию" (add a new element to the collection) in this case.
 */
public class Add implements Command {
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
    public Response makeResponse(ArrayList<Object> params) throws BadRequestException {
        if(params.get(0) instanceof Ticket){
            Ticket ticket = (Ticket) params.get(0);
            ticket.setId(server.getIdCounter().getIdForTicket(ticket));
            if(ticket.getEvent()!=null){
                ticket.getEvent().setId(server.getIdCounter().getIdForEvent(ticket.getEvent()));
            }
            server.getListManager().add(ticket);
            return new Response(getName(), "successfully created");
        }
        throw new BadRequestException("need a Ticket");
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String description() {
        return "добавить новый элемент в коллекцию";
    }
}
