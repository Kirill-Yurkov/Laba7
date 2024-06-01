package server.managers;

import commons.exceptions.BadResponseException;
import commons.utilities.ResponseOfException;
import commons.exceptions.ServerMainResponseException;
import commons.utilities.Request;
import commons.utilities.Response;
import server.Server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class TCPServer {
    private static final int PORT = 7777;
    public static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());
    private Server server;
    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка настройки логгера: " + e.getMessage(), e);
        }
    }

    public TCPServer(Server server){
        this.server = server;
    }

    public void openConnection(){
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Сервер запущен и ожидает подключения на порту " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Клиент подключился: " + clientSocket.getInetAddress());
                    new ClientHandler(clientSocket, server).start();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Ошибка при подключении клиента: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка запуска сервера: " + e.getMessage(), e);
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            for (; ; ) {
                try {
                    Object inputObject = in.readObject();
                    if (inputObject instanceof Request) {
                        Request request = (Request) inputObject;
                        try {
                            Response response = server.invoke(request);
                            out.writeObject(response);
                            TCPServer.LOGGER.info("Выполнена команда: " + response.getName() + " от клиента " + clientSocket);
                        } catch (ServerMainResponseException e){
                            ResponseOfException responseOfException = new ResponseOfException(request.getName(), e);
                            out.writeObject(responseOfException);
                            TCPServer.LOGGER.info("Выполнена команда: " + responseOfException.getName() + " от клиента " + clientSocket);
                        }
                        out.flush();
                    } else {
                        System.out.println(inputObject);
                        out.writeObject(new ResponseOfException("WRONG", new BadResponseException("Неверный запрос")));
                        TCPServer.LOGGER.warning("Получен неверный объект от клиента " + clientSocket);
                    }
                } catch (EOFException | SocketException ignored) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            TCPServer.LOGGER.log(Level.SEVERE, "Ошибка в обработке клиента: " + e.getMessage(), e);
        } finally {
            server.getReaderWriter().writeXML(server.getFileManager().getFilePath(), server.getListManager().getTicketList());
            try {
                clientSocket.close();
            } catch (IOException e) {
                TCPServer.LOGGER.log(Level.SEVERE, "Ошибка при закрытии соединения с клиентом: " + e.getMessage(), e);
            }
            TCPServer.LOGGER.info("Клиент отключился: " + clientSocket.getInetAddress());
        }
    }
}

