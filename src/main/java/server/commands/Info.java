package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;

import java.util.ArrayList;

/**
 * The Info class implements the Command interface and represents a command to display information about the collection.
 * It provides methods to get the command value, set the server, execute the command, get the command name, and get the command description.
 * <p>
 * The execute method retrieves the collection information from the server's reader writer and returns it as a string.
 * If the collection information is empty, it throws a CommandCollectionZeroException.
 * <p>
 * The getName method returns the name of the command as "info".
 * <p>
 * The description method returns a description of the command as "вывести в стандартный поток вывода информацию о коллекции".
 */
public class Info implements Command {
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
        if (server.getReaderWriter().getCollectionInfo().isEmpty()) {
            throw new CommandCollectionZeroException("Collection information will be updating in next save");
        }
        StringBuilder str = new StringBuilder();
        for (String i : server.getReaderWriter().getCollectionInfo()) {
            str.append("\n").append(i);
        }
        return new Response(getName(), str.toString());
    }


    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String description() {
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}
