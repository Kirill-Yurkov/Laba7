package commons.patternclass;

import lombok.Getter;

import java.io.Serializable;

/**
 * Represents the different types of tickets available.
 *
 * This enum class defines three ticket types:
 * - VIP: Represents a VIP ticket with priority 0.
 * - USUAL: Represents a usual ticket with priority 1.
 * - CHEAP: Represents a cheap ticket with priority 2.
 *
 * Each ticket type has a corresponding priority value, which determines the order in which tickets are processed.
 *
 * This class is used to categorize tickets and assign them a priority value for processing purposes.
 */
@Getter
public enum TicketType implements Serializable {
    VIP(0),
    USUAL(1),
    CHEAP(2);
    private final int priority;

    TicketType(int priority) {
        this.priority = priority;
    }

}