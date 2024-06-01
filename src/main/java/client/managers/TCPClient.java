package client.managers;

import client.Client;
import commons.exceptions.BadResponseException;
import commons.utilities.Request;
import commons.utilities.Response;
import commons.utilities.ResponseOfException;

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
            LOGGER.log(Level.SEVERE, "Ошибка настройки логгера: " + e.getMessage(), e);
        }
    }
    public TCPClient(Client client){
        this.client = client;
    }
    public void openConnection() {
        client.getInputOutput().outPut("Подключение к серверу...\n");
        if (!checkConnection()) {
            client.stop();
            closeConnection();
        }
    }
    private boolean checkConnection(){
        int attempts = 0;
        while (attempts < MAX_CONNECTION_ATTEMPTS) {
            try {
                socket = new Socket("localhost", 7777);
                inPort = new ObjectInputStream(socket.getInputStream());
                outPort = new ObjectOutputStream(socket.getOutputStream());
                LOGGER.info("Подключение к серверу выполнено. "+socket);
                client.getInputOutput().outPut("Подключение к серверу выполнено\n");
                return true;
            } catch (IOException e) {
                attempts++;
                LOGGER.warning("Не удалось подключиться к серверу. Повторная попытка через 3 секунды...");
                client.getInputOutput().outPut("Не удалось подключиться к серверу. Повторная попытка через 3 секунды...\n");
                try {
                    TimeUnit.MILLISECONDS.sleep(CONNECTION_RETRY_DELAY);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        client.getInputOutput().outPut("Превышено количество попыток подключения. Завершение работы клиента.\n");
        LOGGER.severe("Превышено количество попыток подключения. Завершение работы клиента.");
        return false;
    }

    public String getAnswer(Request request) throws BadResponseException {
        try {
            outPort.writeObject(request);
            outPort.flush();
            Object response = inPort.readObject();
            if (response instanceof Response) {
                LOGGER.info("От сервера: " + response);
                return client.getCommandInvoker().invokeFromResponse((Response) response);
            } else if (response instanceof ResponseOfException) {
                LOGGER.info("Ответ от сервре" + response);
                throw new BadResponseException(client.getCommandInvoker().invokeFromResponseException((ResponseOfException) response));
            } else {
                LOGGER.warning("Неверный формат ответа от сервера.");
                throw new BadResponseException("bad response");
            }
        } catch (EOFException | SocketException e){
            checkConnection();
            return "Переподключаюсь...";
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
            LOGGER.log(Level.SEVERE, "Ошибка при закрытии соединения: " + e.getMessage(), e);
        }
    }
}
