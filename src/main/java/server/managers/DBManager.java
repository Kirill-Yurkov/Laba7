package server.managers;

import commons.exceptions.AuthException;
import commons.patternclass.Coordinates;
import commons.patternclass.Event;
import commons.patternclass.Ticket;
import commons.patternclass.TicketType;
import lombok.Getter;
import lombok.NonNull;
import server.Server;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * The DBManager class is responsible for managing file operations in the server.
 * It provides functionality for setting the file path, initializing the file, and reading/writing XML files.
 * The DBManager class is used by the Server class to handle file-related tasks.
 * <p>
 * Usage:
 * DBManager fileManager = new DBManager(server);
 * fileManager.setFilePath(filePath); // Set the file path
 * fileManager.initializeFile(); // Initialize the file
 * fileManager.readXML(); // Read the XML file
 * fileManager.writeXML(filePath, tickets); // Write the XML file
 * <p>
 * Example:
 * DBManager fileManager = new DBManager(server);
 * fileManager.setFilePath(filePath);
 * fileManager.initializeFile();
 * fileManager.readXML();
 * fileManager.writeXML(filePath, tickets);
 * ITErator iterable
 */
public class DBManager {
    @Getter
    private Server server;
    public static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());

    @Getter
    private List<String> collectionInfo = new ArrayList<>();

    @Getter
    private List<Ticket> collectionTicket = new ArrayList<>();

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);

            /*
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);
            */

            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка настройки логгера: " + e.getMessage(), e);
        }
    }


    private static final String CREATE_USERS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users (" +
            "id SERIAL PRIMARY KEY, " +
            "login TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    private static final String CREATE_TICKETS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS tickets (" +
            "id BIGSERIAL PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "coordinates_x BIGINT NOT NULL," +
            "coordinates_y BIGINT NOT NULL," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "price INTEGER," +
            "ticket_type VARCHAR(5) NOT NULL," +
            "event_id INTEGER," +
            "user_id INTEGER NOT NULL, " +
            "FOREIGN KEY (event_id) REFERENCES events(id)," +
            "FOREIGN KEY (user_id) REFERENCES users(id))";

    private static final String CREATE_EVENTS_TABLE_SQL = "CREATE TABLE IF NOT EXISTS events (" +
            "id SERIAL PRIMARY KEY, " +
            "name TEXT NOT NULL UNIQUE, " +
            "min_age BIGINT, " +
            "tickets_count INTEGER NOT NULL," +
            "description TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";


    private static final String INSERT_TICKET_SQL = "INSERT INTO tickets (name, coordinates_x, coordinates_y, price, ticket_type, event_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_USER_SQL = "INSERT INTO users (login, password) VALUES (?, ?)";
    private static final String INSERT_EVENT_SQL = "INSERT INTO events (name, min_age, tickets_count, description) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USERS = "SELECT id, login, password FROM users";
    private static final String SELECT_USER_BY_LOGIN = "SELECT id, login, password FROM users WHERE login = ?";
    private static final String SELECT_TICKETS = "SELECT id, name, coordinates_x, coordinates_y, created_at, price, ticket_type, event_id, user_id FROM tickets";
    private static final String SELECT_TICKET_BY_ID = "SELECT id, name, coordinates_x, coordinates_y, created_at, price, ticket_type, event_id, user_id FROM tickets WHERE id = ?";
    private static final String SELECT_EVENT_BY_ID = "SELECT id, name, min_age, tickets_count, description FROM events WHERE id = ?";
    private static final String SELECT_EVENT_BY_NAME = "SELECT id, name, min_age, tickets_count, description FROM events WHERE name = ?";
    private static final String DELETE_TICKET_BY_ID = "DELETE FROM tickets WHERE id = ?";
    private static final String UPDATE_ITEM_SQL = "UPDATE tickets SET name = ?, coordinates_x = ?, coordinates_y = ?, created_at = ?, price = ?, ticket_type = ?, event_id = ? WHERE id = ?";


    public DBManager(Server server) {
        this.server = server;
    }

    public void createTable() throws SQLException {
        // Устанавливаем соединение с базой данных и создаем таблицы
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement1 = connection.prepareStatement(CREATE_USERS_TABLE_SQL);
             PreparedStatement statement2 = connection.prepareStatement(CREATE_EVENTS_TABLE_SQL);
             PreparedStatement statement3 = connection.prepareStatement(CREATE_TICKETS_TABLE_SQL)) {
            statement1.executeUpdate();
            statement2.executeUpdate();
            statement3.executeUpdate();
            LOGGER.info("Items table created successfully");
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("Error occurred while creating tables: " + e.getMessage());
            throw new SQLException("error occurred while creating tables: " + e.getMessage());
        }
    }

    public List<Ticket> readDB() {
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return readAllTicketsFromTable();
    }


    public List<Ticket> readAllTicketsFromTable() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKETS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Ticket> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(readTicket(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Ticket readTicket(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getLong("id"));
        ticket.setName(resultSet.getString("name"));
        ticket.setCoordinates(new Coordinates(resultSet.getLong("coordinates_x"), resultSet.getLong("coordinates_y")));
        ticket.setCreationDate(resultSet.getDate("created_at"));
        ticket.setPrice((Integer) resultSet.getObject("price"));
        ticket.setType(TicketType.valueOf(resultSet.getString("ticket_type")));
        if (resultSet.getObject("event_id") != null) {
            ticket.setEvent(readEventFromTableById(resultSet.getInt("event_id")));
        } else {
            ticket.setEvent(null);
        }
        return ticket;
    }

    public Ticket readTicketFromTableById(int ticketId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKET_BY_ID)) {

            preparedStatement.setInt(1, ticketId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return readTicket(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Event readEventFromTableById(int eventId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EVENT_BY_ID)) {

            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Event event = new Event(
                            resultSet.getString("name"),
                            (Long) resultSet.getObject("min_age"),
                            resultSet.getInt("tickets_count"),
                            (String) resultSet.getObject("description")
                    );
                    event.setId(eventId);
                    return event;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @NonNull
    public Long writeTicketWithoutId(Ticket ticket, int idUser) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TICKET_SQL, new String[]{"id"})) {
            preparedStatement.setString(1, ticket.getName());
            preparedStatement.setLong(2, ticket.getCoordinates().getX());
            preparedStatement.setLong(3, ticket.getCoordinates().getY());
            preparedStatement.setObject(4, ticket.getPrice(), Types.INTEGER);
            preparedStatement.setString(5, ticket.getType().name());
            System.out.println(ticket.getEvent());
            Integer abc = writeEventWithCheckForExist(ticket.getEvent());
            System.out.println(abc);
            preparedStatement.setObject(6, abc, Types.INTEGER);
            preparedStatement.setInt(7, idUser);
            // Выполняем запрос для вставки данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Добавлено атрибутов " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("Error occurred while inserting tickets: " + e.getMessage());
        }
        return null;
    }

    public Integer writeEventWithCheckForExist(Event event) {
        if (event == null) {
            return null;
        }
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EVENT_BY_NAME)) {
            preparedStatement.setString(1, event.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
            return writeEventWithoutId(event);
        } catch (SQLException ignored) {
            return writeEventWithoutId(event);
        }
    }

    private Integer writeEventWithoutId(Event event) {
        if (event == null) {
            return null;
        }
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EVENT_SQL, new String[]{"id"})) {
            preparedStatement.setString(1, event.getName());
            preparedStatement.setObject(2, event.getMinAge(), Types.BIGINT);
            preparedStatement.setInt(3, event.getTicketsCount());
            preparedStatement.setObject(4, event.getDescription(), Types.VARCHAR);
            // Выполняем запрос для вставки данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Добавлено атрибутов " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("Error occurred while inserting tickets: " + e.getMessage());
            System.err.println(e.getMessage());
        }
        return null;
    }

    private Integer writeUserWithoutId(String login, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, new String[]{"id"});
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Добавлено атрибутов " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("error while get id for user by login: " + e.getMessage());
        }
        return null;
    }

    public void updateTicketById(Ticket ticket, long ticketId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ITEM_SQL)) {

            // Устанавливаем параметры запроса
            preparedStatement.setString(1, ticket.getName());
            preparedStatement.setLong(2, ticket.getCoordinates().getX());
            preparedStatement.setLong(3, ticket.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            preparedStatement.setObject(5, ticket.getPrice(), Types.BIGINT);
            preparedStatement.setString(6, ticket.getType().name());
            preparedStatement.setObject(7, writeEventWithCheckForExist(ticket.getEvent()), Types.INTEGER);
            preparedStatement.setLong(8, ticketId);
            // Выполняем запрос для обновления данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Item updated: " + rowsAffected);

        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("Error occurred while updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteTicketById(long ticketId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET_BY_ID)) {

            // Устанавливаем параметр запроса
            preparedStatement.setLong(1, ticketId);

            // Выполняем запрос для удаления данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Item deleted: " + rowsAffected);

        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.severe("Error occurred while deleting item: " + e.getMessage());

        }
    }

    public Integer checkAuth(String login, String password) throws AuthException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                if (login.equals(resultSet.getString("login"))) {
                    if (password.equals(resultSet.getString("password"))) {
                        return resultSet.getInt("id");
                    } else {
                        throw new AuthException("incorrect password");
                    }
                }
            }
            return writeUserWithoutId(login, password);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
