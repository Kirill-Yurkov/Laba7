package server.managers;

import commons.patternclass.Ticket;
import lombok.Getter;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ListManager class is responsible for managing the ticket list in the server.
 * It provides functionality for setting the ticket list, adding and removing tickets,
 * and reading the ticket list from the file. The ListManager class is used by the Server class
 * to handle operations related to the ticket list.
 * <p>
 * Usage:
 * ListManager listManager = new ListManager(server);
 * listManager.setTicketList(tickets); // Set the ticket list
 * listManager.add(ticket); // Add a ticket to the list
 * listManager.remove(ticket); // Remove a ticket from the list
 * listManager.readTicketList(); // Read the ticket list from the file
 * <p>
 * Example:
 * ListManager listManager = new ListManager(server);
 * listManager.setTicketList(tickets);
 * listManager.add(ticket);
 * listManager.remove(ticket);
 * listManager.readTicketList();
 */
@Getter
public class ListManager {
    private List<Ticket> ticketList = new ArrayList<>();
    private Server server;

    public ListManager(Server server) {
        this.server = server;
    }

    public void setTicketList(List<Ticket> tickets) {
        ticketList = tickets;
        initializeCollection();
    }

    public void add(Ticket ticket) {
        ticketList.add(ticket);
        initializeCollection();
    }

    public void remove(Ticket ticket) {
        ticketList.remove(ticket);
        server.getIdCounter().replaceTicketByID(ticket.getId(), null);
        initializeCollection();
    }

    public void readTicketList() {
        ticketList = server.getReaderWriter().getCollectionTicket();
        initializeCollection();
    }

    private void initializeCollection() {
        server.getIdCounter().initializeIdTickets();
        server.getIdCounter().initializeIdEvents();
        Collections.sort(ticketList);
    }
}
