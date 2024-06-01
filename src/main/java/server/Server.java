package server;

import commons.exceptions.BadRequestException;
import commons.exceptions.ServerMainResponseException;
import commons.utilities.Request;
import commons.utilities.Response;
import lombok.Getter;
import lombok.Setter;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import server.managers.*;
import server.utilities.IdCounter;

import java.io.*;

/**
 * The Server class represents a server that handles commands and manages various components.
 * It provides functionality for starting and stopping the server, as well as invoking commands.
 * The server consists of a CommandInvoker, FileManager, ListManager, IdCounter, and TicketCreator.
 * It also has a FileManager.ReaderWriter and FileManager.InputOutput for file operations and input/output handling.
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
    private final FileManager fileManager = new FileManager(this);
    private final ListManager listManager = new ListManager(this);
    private final IdCounter idCounter = new IdCounter(this);
    private final TCPServer tcpServer = new TCPServer(this);
    private final FileManager.ReaderWriter readerWriter = fileManager.new ReaderWriter();
    private final FileManager.InputOutput inputOutput = fileManager.new InputOutput();
    private boolean serverOn;
    @Getter
    @Setter
    private boolean withFile = false;

    public Server(BufferedReader reader, BufferedOutputStream writer) {
        inputOutput.setReader(reader);
        inputOutput.setWriter(writer);
    }

    public static void main(String[] args) {
        Server server = new Server(new BufferedReader(new InputStreamReader(System.in)), new BufferedOutputStream(System.out));
        if (args.length == 1 ) {
            server.getFileManager().initializeFile(args[0]);
        } else{
            server.getFileManager().initializeFile("");
        }
        server.tcpServer.openConnection();
    }
    public void stop() {
        serverOn = false;
        try {
            inputOutput.getReader().close();
            inputOutput.getWriter().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response invoke(Request request) throws ServerMainResponseException {
        try {
            return commandInvoker.invoke(request);
        } catch (CommandValueException e) {
            throw new ServerMainResponseException("incorrect value of command: " + e.getMessage());
        } catch (NullPointerException ignored) {
            throw new ServerMainResponseException("incorrect command");
        } catch (CommandCollectionZeroException e) {
            throw new ServerMainResponseException("command is useless: " + e.getMessage());
        } catch (BadRequestException e){
            throw new ServerMainResponseException("wrong values of request: "+ e.getMessage());
        }
    }
}
