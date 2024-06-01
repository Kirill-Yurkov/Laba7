package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The Save class represents a command to save the collection to a file.
 * It implements the Command interface.
 *
 * This class has the following methods:
 * - getValue(): Returns the CommandValues enum value associated with this command.
 * - setServer(Server server): Sets the server instance for this command.
 * - execute(String value): Executes the save command by writing the collection to a file.
 * - getName(): Returns the name of the command.
 * - description(): Returns a description of the command.
 */
public class Save implements Command {
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
        server.getReaderWriter().writeXML(server.getFileManager().getFilePath(), server.getListManager().getTicketList());
        return new Response(getName(), "successfully");
    }


    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String description() {
        return "сохранить коллекцию в файл";
    }
}
