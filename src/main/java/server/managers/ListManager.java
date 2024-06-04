package server.managers;

import commons.patternclass.Ticket;
import lombok.Getter;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        ticketList = server.getDBManager().getCollectionTicket();
        initializeCollection();
    }

    private void initializeCollection() {
        server.getIdCounter().initializeIdTickets();
        server.getIdCounter().initializeIdEvents();
        Collections.sort(ticketList);
    }
}
