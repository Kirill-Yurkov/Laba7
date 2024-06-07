package server.commands;

import commons.exceptions.BadRequestException;
import commons.exceptions.CommandValueException;
import commons.responses.ResponseOfCommand;
import server.Server;
import server.commands.interfaces.Command;
import commons.exceptions.CommandCollectionZeroException;
import commons.utilities.CommandValues;

import java.sql.SQLException;
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
    public ResponseOfCommand makeResponse(ArrayList<Object> params, int userId) throws CommandValueException, CommandCollectionZeroException, BadRequestException {
        try {
            if(server.getListManager().getTicketListOfAll().isEmpty()){
                throw new CommandCollectionZeroException("collection is zero");
            }
            Collections.shuffle(server.getListManager().getTicketListOfAll());
            return new ResponseOfCommand(getName(),"successfully shuffled");
        } catch (SQLException e){
            throw new BadRequestException("error on data base");
        }

    }



    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String description() {
        return "shuffle the collection tickets in random order";
    }
}
