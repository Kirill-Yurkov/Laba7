package commons.patternclass;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The 'Event' class represents an event with various attributes.
 *
 * Attributes:
 * - id: Integer (required) - The unique identifier for the event. Cannot be null and must be greater than 0.
 * - name: String (required) - The name of the event. Cannot be null and cannot be an empty string.
 * - minAge: Long (optional) - The minimum age requirement for the event. Can be null.
 * - ticketsCount: int (required) - The number of tickets available for the event. Must be greater than 0.
 * - description: String (optional) - The description of the event. Cannot be an empty string and can be null.
 *
 * Constructors:
 * - Event(String name, Long minAge, int ticketsCount, String description) - Creates a new Event object with the given attributes.
 *
 * Methods:
 * - toString(): String - Returns a string representation of the Event object.
 */
@Setter
@Getter
public class Event implements Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long minAge; //Поле может быть null
    private int ticketsCount; //Значение поля должно быть больше 0
    private String description; //Строка не может быть пустой, Поле может быть null

    public Event(String name, Long minAge, int ticketsCount, String description) {
        this.name = name;
        this.minAge = minAge;
        this.ticketsCount = ticketsCount;
        this.description = description;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minAge=" + minAge +
                ", ticketsCount=" + ticketsCount +
                ", description='" + description + '\'' +
                '}';
    }
}