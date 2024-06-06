package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.responses.ResponseOfCommand;
import commons.utilities.CommandValues;
import server.Server;
import server.commands.interfaces.Command;

import java.util.ArrayList;

/**
 * The Show class represents a command that displays all elements of the collection in a string representation.
 * It implements the Command interface and provides methods for executing the command, setting the server,
 * getting the command value, getting the command name, and getting the command description.
 * <p>
 * Usage:
 * Show show = new Show();
 * show.setServer(server); // Set the server
 * String result = show.execute(value); // Execute the command and get the result
 * String name = show.getName(); // Get the command name
 * String description = show.description(); // Get the command description
 * <p>
 * Example:
 * Show show = new Show();
 * show.setServer(server);
 * String result = show.execute(value);
 * String name = show.getName();
 * String description = show.description();
 */
public class Show implements Command {
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
    public ResponseOfCommand makeResponse(ArrayList<Object> params, int userId) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        StringBuilder str = new StringBuilder();
        if (server.getListManager().getTicketListOfAll().isEmpty()) {
            throw new CommandCollectionZeroException("collection is empty");
        }
        for (int i = 0; i < server.getListManager().getTicketListOfAll().size(); i++) {
            if (i != server.getListManager().getTicketListOfAll().size() - 1){
                str.append(server.getListManager().getTicketListOfAll().get(i).toString()).append("\n");
            } else{
                str.append(server.getListManager().getTicketListOfAll().get(i).toString());
            }
        }
        return new ResponseOfCommand(getName(), String.valueOf(str));
    }


    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String description() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
