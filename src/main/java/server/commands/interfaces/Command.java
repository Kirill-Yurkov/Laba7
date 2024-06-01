package server.commands.interfaces;

import commons.exceptions.BadRequestException;
import commons.utilities.Response;
import server.Server;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;

import java.util.ArrayList;

public interface Command {
    CommandValues getValue();
    void setServer(Server server);
    Response makeResponse(ArrayList<Object> params) throws CommandValueException, CommandCollectionZeroException, BadRequestException;
    String getName();
    String description();
}
