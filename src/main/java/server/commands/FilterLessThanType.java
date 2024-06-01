package server.commands;

import commons.exceptions.BadRequestException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.patternclass.Ticket;
import commons.patternclass.TicketType;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The FilterLessThanType class represents a command that filters and returns the elements from the ticket list
 * whose type has a lower priority than the specified value.
 *
 * Usage:
 * FilterLessThanType filter = new FilterLessThanType();
 * filter.setServer(server); // Set the server for the command
 * String result = filter.execute(value); // Execute the command with the specified value
 *
 * Example:
 * FilterLessThanType filter = new FilterLessThanType();
 * filter.setServer(server);
 * String result = filter.execute("VIP");
 *
 * Command Name: filter_less_than_type
 * Description: выводит элементы, значение поля type (VIP>USUAL>CHEAP) которых меньше заданного (value)
 */
public class FilterLessThanType implements Command {
    private Server server;
    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE;
    }

    @Override
    public void setServer(Server server) {
        this.server=server;
    }

    @Override
    public Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        if(params.get(0) instanceof TicketType){
            TicketType type = (TicketType) params.get(0);
            StringBuilder str = new StringBuilder();
            if(server.getListManager().getTicketList().isEmpty()){
                throw new CommandCollectionZeroException("collection is empty");
            }
            for(Ticket ticket: server.getListManager().getTicketList()){
                if(type.getPriority()<ticket.getType().getPriority()){
                    str.append(ticket).append("\n");
                }
            }
            return new Response(getName(), String.valueOf(str));
        }
        throw new BadRequestException("need a TicketType");

    }
    @Override
    public String getName() {
        return "filter_less_than_type";
    }

    @Override
    public String description() {
        return "вывести элементы, значение поля type (VIP>USUAL>CHEAP) которых меньше заданного";
    }
}
