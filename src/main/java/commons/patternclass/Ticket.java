package commons.patternclass;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
/**
 * The 'Ticket' class represents a ticket with various attributes.
 *
 * Attributes:
 * - id: long (required) - The unique identifier for the ticket. Must be greater than 0 and must be unique.
 * - name: String (required) - The name of the ticket. Cannot be null and cannot be an empty string.
 * - coordinates: Coordinates (required) - The coordinates of the ticket. Cannot be null.
 * - creationDate: Date (required) - The creation date of the ticket. Cannot be null and is automatically generated.
 * - price: Integer (optional) - The price of the ticket. Can be null and must be greater than 0.
 * - type: TicketType (required) - The type of the ticket. Cannot be null.
 * - event: Event (optional) - The event associated with the ticket. Can be null.
 *
 * Constructors:
 * - Ticket() - Creates a new empty Ticket object.
 * - Ticket(String name, Coordinates coordinates, Date creationDate, Integer price, TicketType type, Event event) - Creates a new Ticket object with the given attributes.
 *
 * Methods:
 * - getEvent(): Event - Returns the event associated with the ticket. Throws NullPointerException if event is null.
 * - setCreationDate(Date creationDate): void - Sets the creation date of the ticket.
 * - compareTo(Ticket ticket): int - Compares the priority of the ticket type with another ticket. Returns a negative integer, zero, or a positive integer as this ticket type is less than, equal to, or greater than the specified ticket type.
 * - toString(): String - Returns a string representation of the Ticket object.
 */
@Getter
@Setter
public class Ticket implements Comparable<Ticket>, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer price; //Поле может быть null, Значение поля должно быть больше 0
    private TicketType type; //Поле не может быть null
    private Event event; //Поле может быть null

    public Ticket() {}

    public Event getEvent() throws NullPointerException {
        return event;
    }

    public Ticket(String name, Coordinates coordinates, Date creationDate, Integer price, TicketType type, Event event) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.type = type;
        this.event = event;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Ticket ticket) {
        return Integer.compare(ticket.type.getPriority(), this.type.getPriority());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", type=" + type +
                ", event=" + event +
                '}';
    }

}