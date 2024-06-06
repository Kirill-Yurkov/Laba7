package server.managers;

import commons.patternclass.Ticket;
import lombok.Getter;
import server.Server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListManager {

    private List<Ticket> ticketList = new ArrayList<>();
    @Getter
    private Server server;

    public ListManager(Server server) {
        this.server = server;
    }
    public List<Ticket> getTicketListOfAll() {
        return ticketList;
    }
    public List<Ticket> getTicketListOfUser(int userId) throws SQLException {
        return server.getDBManager().readTicketsFromTableByUserId(userId);
    }

    public void add(Ticket ticket, int userId) throws SQLException {
        ticket.setId(server.getDBManager().writeTicketWithoutId(ticket, userId));
        ticketList.add(ticket);
    }

    public void remove(Ticket ticket) throws SQLException {
        server.getDBManager().deleteTicketById(ticket.getId());
        ticketList.remove(ticket);
    }
    public void update(Ticket ticket, long ticketId) throws SQLException {
        for(Ticket ticket1: ticketList){
            if(ticket1.getId()==ticketId){
                int index = ticketList.indexOf(ticket1);
                ticketList.remove(ticket1);
                ticketList.add(index, ticket);
            }
        }
        server.getDBManager().updateTicketById(ticket, ticketId);
    }
    public void readTicketList() {
        ticketList = server.getDBManager().readDB();
    }
}
