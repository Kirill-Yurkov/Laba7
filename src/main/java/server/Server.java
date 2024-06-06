package server;

import commons.exceptions.BadRequestException;
import commons.exceptions.ServerMainResponseException;
import commons.requests.RequestOfCommand;
import commons.responses.ResponseOfCommand;
import lombok.Getter;
import lombok.Setter;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import server.managers.*;

/**
 * The Server class represents a server that handles commands and manages various components.
 * It provides functionality for starting and stopping the server, as well as invoking commands.
 * The server consists of a CommandInvoker, DBManager, ListManager, IdCounter, and TicketCreator.
 * It also has a DBManager.ReaderWriter and DBManager.InputOutput for file operations and input/output handling.
 * The server can be started with or without a file, and it handles exceptions related to command execution.
 *
 * Usage:
 * Server server = new Server(reader, writer);
 * server.start(); // Start the server and handle commands from the console
 * server.start(file); // Start the server and handle commands from a file
 * server.stop(); // Stop the server
 *
 * Example:
 * Server server = new Server(reader, writer);
 * server.start();
 *
 */
@Getter
public class Server {
    private final CommandInvoker commandInvoker = new CommandInvoker(this);
    private final DBManager DBManager = new DBManager(this);
    private final ListManager listManager = new ListManager(this);
    private final TCPServer tcpServer = new TCPServer(this);
    @Setter
    private boolean withFile = false;

    public static void main(String[] args) {
        Server server = new Server();
        Server.start(server);
    }

    private static void start(Server server){
        server.listManager.readTicketList();
        server.tcpServer.openConnection();
    }
    public ResponseOfCommand invoke(RequestOfCommand requestOfCommand, int userId) throws ServerMainResponseException {
        try {
            return commandInvoker.invoke(requestOfCommand, userId);
        } catch (CommandValueException e) {
            throw new ServerMainResponseException("incorrect value of command: " + e.getMessage());
        } catch (NullPointerException ignored) {
            throw new ServerMainResponseException("incorrect command");
        } catch (CommandCollectionZeroException e) {
            throw new ServerMainResponseException("command is useless: " + e.getMessage());
        } catch (BadRequestException e){
            throw new ServerMainResponseException("wrong values of requestOfCommand: "+ e.getMessage());
        }
    }
}
