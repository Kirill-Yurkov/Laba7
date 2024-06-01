package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandValueException;
import commons.utilities.Response;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;

import java.util.ArrayList;
import java.util.Collections;
/**
 * The Shuffle class represents a command that shuffles the elements of a collection in random order.
 * It implements the Command interface and provides functionality for executing the shuffle command.
 * The shuffle command requires a non-empty collection to be present in the server's ListManager.
 * If the collection is empty, a CommandCollectionZeroException is thrown.
 *
 * Usage:
 * Shuffle shuffleCommand = new Shuffle();
 * shuffleCommand.setServer(server); // Set the server for the command
 * String result = shuffleCommand.execute(""); // Execute the shuffle command
 *
 * Example:
 * Shuffle shuffleCommand = new Shuffle();
 * shuffleCommand.setServer(server);
 * String result = shuffleCommand.execute("");
 *
 * Command Name: shuffle
 * Description: перемешать элементы коллекции в случайном порядке
 */
public class Shuffle implements Command {
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
        if(server.getListManager().getTicketList().isEmpty()){
            throw new CommandCollectionZeroException("collection is zero");
        }
        Collections.shuffle(server.getListManager().getTicketList());
        return new Response(getName(),"successfully shuffled");
    }



    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String description() {
        return "перемешать элементы коллекции в случайном порядке";
    }
}
