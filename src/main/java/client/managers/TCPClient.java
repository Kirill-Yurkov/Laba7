package client.managers;

import client.Client;
import commons.exceptions.BadResponseException;
import commons.requests.Request;
import commons.responses.ResponseOfCommand;
import commons.responses.ResponseOfException;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class TCPClient {
    private final Client client;
    private static final int CONNECTION_RETRY_DELAY = 5000;
    private static final int MAX_CONNECTION_ATTEMPTS = 3;
    private static final Logger LOGGER = Logger.getLogger(TCPClient.class.getName());

    private Socket socket;
    private ObjectInputStream inPort;
    private ObjectOutputStream outPort;

    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);

            /*
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);
            */

            FileHandler fileHandler = new FileHandler("client.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting up the logger: " + e.getMessage(), e);
        }
    }
    public TCPClient(Client client){
        this.client = client;
    }
    public boolean openConnection() {
        client.getInputOutput().outPut("Connecting to the server...\n");
        if (!checkConnection()) {
            client.stop();
            return false;
        }
        return true;
    }
    private boolean checkConnection(){
        int attempts = 0;
        while (attempts < MAX_CONNECTION_ATTEMPTS) {
            try {
                socket = new Socket("localhost", 6666);
                inPort = new ObjectInputStream(socket.getInputStream());
                outPort = new ObjectOutputStream(socket.getOutputStream());
                LOGGER.info("The connection to the server has been completed. "+socket);
                client.getInputOutput().outPut("The connection to the server has been completed.\n");
                return true;
            } catch (IOException e) {
                attempts++;
                LOGGER.warning("Failed to connect to the server. Try again in 3 seconds...");
                client.getInputOutput().outPut("Failed to connect to the server. Try again in 3 seconds...\n");
                try {
                    TimeUnit.MILLISECONDS.sleep(CONNECTION_RETRY_DELAY);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        client.getInputOutput().outPut("The number of connection attempts has been exceeded. Client shutdown.\n");
        LOGGER.severe("The number of connection attempts has been exceeded. Client shutdown.");
        return false;
    }

    public String getAnswer(Request request) throws BadResponseException {
        try {
            outPort.writeObject(request);
            outPort.flush();
            Object response = inPort.readObject();
            if (response instanceof ResponseOfCommand) {
                LOGGER.info("From server: " + response);
                return client.getCommandInvoker().invokeFromResponse((ResponseOfCommand) response);
            } else if (response instanceof ResponseOfException) {
                LOGGER.info("Answer from server: " + response);
                throw new BadResponseException(client.getCommandInvoker().invokeFromResponseException((ResponseOfException) response));
            } else {
                LOGGER.warning("Invalid response format from the server.");
                throw new BadResponseException("bad response");
            }
        } catch (EOFException | SocketException e){
            if(checkConnection()){
                return "You can work with the server again.";
            }
            client.stop();
            return "It is not connected.";
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new BadResponseException("bad channel");
        }
    }
    public void closeConnection() {
        try {
            if (outPort != null) {
                outPort.close();
            }
            if (inPort != null) {
                inPort.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing the connection: " + e.getMessage(), e);
        }
    }
}
