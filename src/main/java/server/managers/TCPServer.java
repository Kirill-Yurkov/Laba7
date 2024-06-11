package server.managers;

import commons.exceptions.AuthException;
import commons.exceptions.BadResponseException;
import commons.exceptions.ServerMainResponseException;
import commons.requests.RequestAuth;
import commons.requests.RequestAuthOfNotAuthorized;
import commons.requests.RequestOfCommand;
import commons.responses.ResponseOfCommand;
import commons.responses.ResponseOfException;
import server.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.*;

public class TCPServer {
    private static final int PORT = 6666;
    public static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());
    private final Server server;

    private final ExecutorService requestPool = Executors.newFixedThreadPool(10);
    private final ForkJoinPool processingPool = new ForkJoinPool();

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

    public TCPServer(Server server) {
        this.server = server;
    }

    public void openConnection() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Сервер запущен и ожидает подключения на порту " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Клиент подключился: " + clientSocket.getInetAddress());
                    requestPool.submit(new ClientHandler(clientSocket, server, processingPool));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Ошибка при подключении клиента: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка запуска сервера: " + e.getMessage(), e);
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private final ForkJoinPool processingPool;

    public ClientHandler(Socket socket, Server server, ForkJoinPool processingPool) {
        this.clientSocket = socket;
        this.server = server;
        this.processingPool = processingPool;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                try {
                    Object inputObject = in.readObject();
                    if (inputObject instanceof RequestOfCommand requestOfCommand) {
                        processingPool.submit(() -> {
                            try {
                                Integer id = server.getDBManager().checkAuth(requestOfCommand.getLogin(), requestOfCommand.getPassword());
                                ResponseOfCommand responseOfCommand = server.invoke(requestOfCommand, id);
                                TCPServer.LOGGER.info("Выполнена команда: " + responseOfCommand.getName() + " от клиента " + clientSocket);
                                sendResponse(out, responseOfCommand);
                            } catch (ServerMainResponseException | AuthException e) {
                                ResponseOfException responseOfException = new ResponseOfException(requestOfCommand.getName(), e);
                                TCPServer.LOGGER.severe("Ошибка: " + responseOfException.getName() + " для клиента " + clientSocket);
                                sendResponse(out, responseOfException);
                            }
                        });
                    } else if (inputObject instanceof RequestAuth requestAuth) {
                        processingPool.submit(() -> {
                            try {
                                Integer id = server.getDBManager().checkAuth(requestAuth.getLogin(), requestAuth.getPassword());
                                sendResponse(out, new ResponseOfCommand(requestAuth.getLogin(), "successfully authorized"));
                                TCPServer.LOGGER.info(requestAuth.getLogin() + " successfully authorized by id " + id);
                            } catch (AuthException e) {
                                sendResponse(out, new ResponseOfException(requestAuth.getLogin(), e));
                                TCPServer.LOGGER.severe("Ошибка: " + requestAuth.getLogin() + " для клиента " + clientSocket);
                            }
                        });
                    } else if (inputObject instanceof RequestAuthOfNotAuthorized requestAuthOfNotAuthorized) {
                        processingPool.submit(() -> {
                            try {
                                Integer id = server.getDBManager().checkAuthForNotAuthorized(requestAuthOfNotAuthorized.getLogin(), requestAuthOfNotAuthorized.getPassword());
                                sendResponse(out, new ResponseOfCommand(requestAuthOfNotAuthorized.getLogin(), "successfully authorized"));
                                TCPServer.LOGGER.info(requestAuthOfNotAuthorized.getLogin() + " successfully authorized by id " + id);
                            } catch (AuthException e) {
                                sendResponse(out, new ResponseOfException(requestAuthOfNotAuthorized.getLogin(), e));
                                TCPServer.LOGGER.severe("Ошибка: " + requestAuthOfNotAuthorized.getLogin() + " для клиента " + clientSocket);
                            }
                        });
                    } else {
                        ResponseOfException responseOfException = new ResponseOfException("WRONG", new BadResponseException("Неверный запрос"));
                        sendResponse(out, responseOfException);
                    }
                } catch (EOFException | SocketException ignored) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            TCPServer.LOGGER.log(Level.SEVERE, "Ошибка в обработке клиента: " + e.getMessage(), e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                TCPServer.LOGGER.log(Level.SEVERE, "Ошибка при закрытии соединения с клиентом: " + e.getMessage(), e);
            }
            TCPServer.LOGGER.info("Клиент отключился: " + clientSocket.getInetAddress());
        }
    }

    private void sendResponse(ObjectOutputStream out, ResponseOfCommand responseOfCommand) {
        new Thread(() -> {
            try {
                out.writeObject(responseOfCommand);
                out.flush();
            } catch (IOException e) {
                TCPServer.LOGGER.log(Level.SEVERE, "Ошибка при отправке ответа клиенту: " + e.getMessage(), e);
            }
        }).start();
    }

    private void sendResponse(ObjectOutputStream out, ResponseOfException response) {
        new Thread(() -> {
            try {
                out.writeObject(response);
                out.flush();

            } catch (IOException e) {
                TCPServer.LOGGER.log(Level.SEVERE, "Ошибка при отправке ответа клиенту: " + e.getMessage(), e);
            }
        }).start();
    }

}