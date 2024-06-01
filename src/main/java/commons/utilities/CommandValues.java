package commons.utilities;

import java.io.Serializable;

/**
 * The CommandValues class represents the possible values for a command.
 * It is an enumeration that includes the following values:
 * - NOTHING: Represents no value or command.
 * - VALUE: Represents a single value.
 * - ELEMENT: Represents a single element.
 * - VALUE_ELEMENT: Represents a combination of a value and an element.
 */
public enum CommandValues implements Serializable {
    NOTHING,
    VALUE,
    ELEMENT,
    VALUE_ELEMENT;
}
