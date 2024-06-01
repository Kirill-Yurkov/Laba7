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
 * The Exit class represents a command to exit the program without saving to a file.
 * It implements the Command interface and provides the necessary methods to execute the command.
 * The command value is CommandValues.NOTHING.
 * The execute method stops the server by calling the stop method of the Server class.
 * The getName method returns the name of the command, which is "exit".
 * The description method returns a description of the command, which is "завершить программу (без сохранения в файл)".
 */
public class Exit implements Command {
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
        return "exit";
    }

    @Override
    public String description() {
        return "завершить программу (без сохранения в файл)";
    }
}
