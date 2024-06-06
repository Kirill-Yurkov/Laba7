package server.commands.interfaces;

import commons.exceptions.BadRequestException;
import commons.responses.ResponseOfCommand;
import server.Server;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;

import java.util.ArrayList;

public interface Command {
    CommandValues getValue();
    void setServer(Server server);
    ResponseOfCommand makeResponse(ArrayList<Object> params, int userId) throws CommandValueException, CommandCollectionZeroException, BadRequestException;
    String getName();
    String description();
}
