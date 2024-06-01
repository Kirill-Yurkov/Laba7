package client.commands;

import client.commands.interfaces.Command;
import client.Client;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;
import commons.utilities.Request;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * The ExecuteScript class represents a command that reads and executes a script from a specified file.
 * The script contains commands in the same format as the user enters them in interactive mode.
 *
 * This class implements the Command interface and provides the following methods:
 * - getValue(): Returns the value of the command.
 * - setServer(Client client): Sets the client for the command.
 * - execute(String filePath): Executes the script from the specified file.
 * - checkFilePermission(String filePath): Checks the file permission for the specified file.
 * - getName(): Returns the name of the command.
 * - description(): Returns the description of the command.
 *
 * The ExecuteScript class is used in the client application to handle the "execute_script" command.
 * This command allows the client to read and execute a script from a file, which can be useful for automating tasks or running multiple commands at once.
 * The execute() method reads the script file, checks the file permission, and executes the commands in the script.
 * If the file has recursion, a CommandValueException is thrown.
 * If the file is not found or the file permission is denied, a CommandValueException is thrown.
 *
 * Example usage:
 * ExecuteScript executeScript = new ExecuteScript();
 * executeScript.setServer(client);
 * String result = executeScript.execute(filePath);
 *
 * Note: This class assumes that the Client class and the Command interface are already defined.
 */
public class ExecuteScript implements Command {
    private Client client;
    private static Set<String> fileSet = new HashSet<>();

    @Override
    public CommandValues getValue() {
        return CommandValues.VALUE;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Request makeRequest(String filePath) throws CommandValueException {

        if (checkFilePermission(filePath)) {
            if(!fileSet.contains(filePath)){
                fileSet.add(filePath);
                client.setWithFile(true);
                client.start(new File(filePath));
                fileSet.remove(filePath);
                client.setWithFile(false);
                ArrayList<Object> params = new ArrayList<>();
                params.add(filePath);
                return new Request(getName(),getValue(),params);
            } else {
                client.setWithFile(false);
                throw new CommandValueException("file has recursion");
            }

        }else {
            client.setWithFile(false);
            throw new CommandValueException("error");
        }

    }
    private boolean checkFilePermission(String filePath) throws  CommandValueException {
        try {
            File file = new File(filePath);
        } catch (SecurityException e) {
            throw new CommandValueException("file permission denied");
        } catch (Exception e) {
            throw new CommandValueException("file not found");
        }
        return true;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String description() {
        return "<String> cчитать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
