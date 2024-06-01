package server.utilities;

import lombok.Getter;
import server.Server;
import commons.patternclass.Event;
import commons.patternclass.Ticket;

import java.util.HashMap;
/**
 * The IdCounter class is responsible for managing the IDs of tickets and events in the server.
 * It provides functionality for generating and replacing IDs, as well as initializing the ID collections.
 * The IdCounter class is used by the ListManager class to handle operations related to ticket and event IDs.
 *
 * Usage:
 * IdCounter idCounter = new IdCounter(server);
 * long ticketId = idCounter.getIdForTicket(ticket); // Generate an ID for a ticket
 * idCounter.replaceTicketByID(ticketId, ticket); // Replace the ID of a ticket
 * idCounter.initializeIdTickets(); // Initialize the ticket ID collection
 * int eventId = idCounter.getIdForEvent(event); // Generate an ID for an event
 * idCounter.initializeIdEvents(); // Initialize the event ID collection
 *
 * Example:
 * IdCounter idCounter = new IdCounter(server);
 * long ticketId = idCounter.getIdForTicket(ticket);
 * idCounter.replaceTicketByID(ticketId, ticket);
 * idCounter.initializeIdTickets();
 * int eventId = idCounter.getIdForEvent(event);
 * idCounter.initializeIdEvents();
 *
 */
public class IdCounter {
    private final HashMap<Long, Ticket> idTickets = new HashMap<>();
    private final HashMap<Integer, Event> idEvents = new HashMap<>();
    @Getter
    private Server server;

    public IdCounter(Server server) {
        this.server = server;
    }

    public long getIdForTicket(Ticket ticket) {
        for (long i : idTickets.keySet()) {
            if (idTickets.get(i) == null) {
                idTickets.put(i, ticket);
                return i;
            }
        }
        idTickets.put((long) (idTickets.size() + 1), ticket);
        return idTickets.size();
    }
    public void replaceTicketByID(long i, Ticket ticket) {
        idTickets.put(i, ticket);
        if(ticket != null){
            ticket.setId(i);
        }
    }

    public void initializeIdTickets() {
        long maxi = 0;
        for (Ticket ticket : server.getListManager().getTicketList()) {
            if (ticket.getId() > maxi) {
                maxi = ticket.getId();
            }
        }
        for (long i = 1; i < Math.max(maxi, idTickets.size() + 1); i++) {
            idTickets.put(i, null);
        }
        for (Ticket ticket : server.getListManager().getTicketList()) {
            idTickets.put(ticket.getId(), ticket);
        }
    }

    public int getIdForEvent(Event event) {
        for (int i : idEvents.keySet()) {
            if (idEvents.get(i) == null) {
                idEvents.put(i, event);
                return i;
            }
        }
        idEvents.put(idEvents.size() + 1, event);
        return idEvents.size();
    }

    public void initializeIdEvents() {
        int maxi = 0;
        for (Ticket ticket : server.getListManager().getTicketList()) {
            try {
                if (ticket.getEvent().getId() > maxi) {
                    maxi = ticket.getEvent().getId();
                }
            } catch (NullPointerException ignored) {

            }

        }
        for (int i = 1; i < Math.max(maxi, idEvents.size() + 1); i++) {
            idEvents.put(i, null);
        }
        for (Ticket ticket : server.getListManager().getTicketList()) {
            try {
                idEvents.put(ticket.getEvent().getId(), ticket.getEvent());
            } catch (NullPointerException ignored) {
            }
        }
    }
}
