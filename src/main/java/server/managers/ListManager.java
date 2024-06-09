package server.managers;

import commons.patternclass.Ticket;
import lombok.Getter;
import server.Server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ListManager {

    private List<Ticket> ticketList = new ArrayList<>();
    @Getter
    private final Server server;
    private final ReentrantLock lock = new ReentrantLock();

    public ListManager(Server server) {
        this.server = server;
    }
    public List<Ticket> getTicketListOfAll() throws SQLException {
        lock.lock();
        try {
            return server.getDBManager().readAllTicketsFromTable();
        }finally {
            lock.unlock();
        }
    }
    public List<Ticket> getTicketListOfUser(int userId) throws SQLException {
        lock.lock();
        try {
            return server.getDBManager().readTicketsFromTableByUserId(userId);
        }finally {
            lock.unlock();
        }
    }

    public void add(Ticket ticket, int userId) throws SQLException {
        lock.lock();
        try {
            ticket.setId(server.getDBManager().writeTicketWithoutId(ticket, userId));
            ticketList.add(ticket);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Ticket ticket) throws SQLException {
        lock.lock();
        try {
            server.getDBManager().deleteTicketById(ticket.getId());
            ticketList.remove(ticket);
        }finally {
            lock.unlock();
        }

    }
    public void update(Ticket ticket, long ticketId) throws SQLException {
        lock.lock();
        try {
            for(Ticket ticket1: ticketList){
                if(ticket1.getId()==ticketId){
                    int index = ticketList.indexOf(ticket1);
                    ticketList.remove(ticket1);
                    ticketList.add(index, ticket);
                }
            }
            server.getDBManager().updateTicketById(ticket, ticketId);
        }finally {
            lock.unlock();
        }

    }
    public void readTicketList() throws SQLException {
        lock.lock();
        try {
            ticketList = server.getDBManager().readDB();
        }finally {
            lock.unlock();
        }
    }
}
