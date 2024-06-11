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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class DBManager {
    @Getter
    private final Server server;
    public static final Logger LOGGER = Logger.getLogger(TCPServer.class.getName());

    @Getter
    private final List<String> collectionInfo = new ArrayList<>();

    @Getter
    private final List<Ticket> collectionTicket = new ArrayList<>();

    private static final String URL = "jdbc:postgresql://pg:5432/studs";
    // jdbc:postgresql://localhost:5432/postgres
    // jdbc:postgresql://pg:5432/studs
    private static final String USER = "s409936";
    // postgres
    // s409936
    private static final String PASSWORD = "fVbN4A91DGSYl7CG";
    // 123
    // fVbN4A91DGSYl7CG

    static {
        try {
            LogManager.getLogManager().reset();
            LOGGER.setLevel(Level.ALL);
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error setting up the logger: " + e.getMessage(), e);
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
    private static final String SELECT_TICKETS_BY_USER = "SELECT id, name, coordinates_x, coordinates_y, created_at, price, ticket_type, event_id, user_id FROM tickets Where user_id = ?";
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
            LOGGER.log(Level.SEVERE,"Error occurred while creating tables: " + e.getMessage(), e);
            throw new SQLException("error occurred while creating tables: " + e.getMessage());
        }
    }

    public List<Ticket> readDB() throws SQLException {
        createTable();
        return readAllTicketsFromTable();
    }


    public List<Ticket> readAllTicketsFromTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKETS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Ticket> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(readTicket(resultSet));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while reading ALL tickets: " + e.getMessage(), e);
            throw new SQLException(e.getMessage());
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

    public Ticket readTicketFromTableById(int ticketId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKET_BY_ID)) {

            preparedStatement.setInt(1, ticketId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return readTicket(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while reading ticket from table by ticket id: " + e.getMessage(), e);
            throw new SQLException();
        }
        return null;
    }
    public List<Ticket> readTicketsFromTableByUserId(int userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKETS_BY_USER)) {
            List<Ticket> tickets = new ArrayList<>();
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tickets.add(readTicket(resultSet));
                }
            }
            return tickets;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while reading tickets from table by userID: " + e.getMessage(), e);
            throw new SQLException();
        }
    }

    public Event readEventFromTableById(int eventId) throws SQLException {
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
            LOGGER.log(Level.SEVERE,"Error occurred while reading event: " + e.getMessage(), e);
            throw new SQLException();
        }
        return null;
    }

    public Long writeTicketWithoutId(Ticket ticket, int idUser) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TICKET_SQL, new String[]{"id"})) {
            preparedStatement.setString(1, ticket.getName());
            preparedStatement.setLong(2, ticket.getCoordinates().getX());
            preparedStatement.setLong(3, ticket.getCoordinates().getY());
            preparedStatement.setObject(4, ticket.getPrice(), Types.INTEGER);
            preparedStatement.setString(5, ticket.getType().name());
            Integer abc = writeEventWithCheckForExist(ticket.getEvent());
            preparedStatement.setObject(6, abc, Types.INTEGER);
            preparedStatement.setInt(7, idUser);
            // Выполняем запрос для вставки данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Added attributes " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.log(Level.SEVERE,"Error occurred while inserting tickets: " + e.getMessage(), e);
            throw new SQLException();
        }
        return null;
    }

    public Integer writeEventWithCheckForExist(Event event) throws SQLException {
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while inserting event: " + e.getMessage(), e);
            throw new SQLException();
        }
    }

    private Integer writeEventWithoutId(Event event) throws SQLException {
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
            LOGGER.info("Added attributes " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.log(Level.SEVERE,"Error occurred while inserting events without id: " + e.getMessage(), e);
            throw new SQLException();
        }
        return null;
    }

    private Integer writeUserWithoutId(String login, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, new String[]{"id"});
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, sha1Hash(password));
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Added attributes " + rowsAffected);
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.log(Level.SEVERE,"Error occurred while get id for user by login: " + e.getMessage(), e);
            throw new SQLException();
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
            LOGGER.info("Ticket updated: " + rowsAffected);

        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.log(Level.SEVERE,"Error occurred while updating ticket by Id: " + e.getMessage(), e);
            throw new SQLException();
        }
    }

    public void deleteTicketById(long ticketId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TICKET_BY_ID)) {

            // Устанавливаем параметр запроса
            preparedStatement.setLong(1, ticketId);

            // Выполняем запрос для удаления данных
            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("Ticket deleted: " + rowsAffected);

        } catch (SQLException e) {
            // Обработка исключений SQL
            LOGGER.log(Level.SEVERE,"Error occurred while deleting ticket: " + e.getMessage(), e);
            throw new SQLException();
        }
    }

    public Integer checkAuth(String login, String password) throws AuthException {
        if(login.isBlank()||password.isBlank()){
            throw new AuthException("empty strings");
        }
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                if (login.equals(resultSet.getString("login"))) {
                    if (sha1Hash(password).equals(resultSet.getString("password"))) {
                        return resultSet.getInt("id");
                    } else {
                        throw new AuthException("incorrect password");
                    }
                }
            }
            throw new AuthException("incorrect login");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while checking auth: " + e.getMessage(), e);
            return null;
        }
    }
    public Integer checkAuthForNotAuthorized(String login, String password) throws AuthException {
        if(login.isBlank()||password.isBlank()){
            throw new AuthException("empty strings");
        }
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                if (login.equals(resultSet.getString("login"))) {
                    throw new AuthException("this login is already taken.");
                }
            }
            return writeUserWithoutId(login, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error occurred while checking auth: " + e.getMessage(), e);
            return null;
        }
    }
    private static String sha1Hash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
