package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.requests.RequestOfCommand;
import commons.utilities.CommandValues;

import java.util.ArrayList;

/**
 * The Help class represents a command that provides help and information about available commands.
 * It implements the Command interface.
 *
 * The Help class has the following functionality:
 * - Getting the command value: It returns the CommandValues.NOTHING value, indicating that no value is expected for this command.
 * - Setting the client: It sets the client instance for the command.
 * - Executing the command: It generates a string that contains information about all available commands.
 * - Getting the command name: It returns the name of the command, which is "help".
 * - Getting the command description: It returns a string that describes the purpose of the command.
 *
 * Example usage:
 * Help helpCommand = new Help();
 * helpCommand.setServer(client);
 * String result = helpCommand.execute("");
 * String commandName = helpCommand.getName();
 * String commandDescription = helpCommand.description();
 *
 * Note: The Help class assumes that the client instance is set before executing the command.
 *
 * @see Command
 * @see CommandValues
 * @see Client
 */
public class Help implements Command {
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
        StringBuilder str = new StringBuilder();
        for(Command command: client.getCommandInvoker().getCommands()){
            str.append("\n").append(command.getName()).append(" : ").append(command.description());
        }
        client.getInputOutput().outPut(String.valueOf(str));
        return new RequestOfCommand(getName(),getValue(), new ArrayList<Object>(), client.getLogin(), client.getPassword());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String description() {
        return "<> output help for available commands";
    }
}
