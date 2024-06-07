package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.requests.RequestOfCommand;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The Exit class represents a command to exit the program without saving to a file.
 * It implements the Command interface and provides the necessary methods to execute the command.
 * The command value is CommandValues.NOTHING.
 * The execute method stops the client by calling the stop method of the Client class.
 * The getName method returns the name of the command, which is "exit".
 * The description method returns a description of the command, which is "завершить программу (без сохранения в файл)".
 */
public class Exit implements Command {
    private Client client;

    @Override
    public CommandValues getValue() {
        return CommandValues.NOTHING;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public RequestOfCommand makeRequest(String value){
        client.stop();
        return new RequestOfCommand(getName(),getValue(), new ArrayList<>(), client.getLogin(), client.getPassword());
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String description() {
        return "<> end the program";
    }
}
