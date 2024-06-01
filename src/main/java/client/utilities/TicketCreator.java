package client.utilities;

import client.Client;
import commons.exceptions.StopCreateTicketExceptionByClient;
import commons.utilities.Validator;
import lombok.Getter;
import commons.patternclass.Coordinates;
import commons.patternclass.Event;
import commons.patternclass.Ticket;
import commons.patternclass.TicketType;

import java.util.Date;
import java.util.Random;
/**
 * The TicketCreator class is responsible for creating Ticket objects.
 * It provides methods to create a Ticket group either by taking user input or generating random values.
 * The class uses a Client object to interact with the user for input and output operations.
 *
 * The TicketCreator class has the following attributes:
 * - client: A Client object used for communication with the user.
 * - isExit: A boolean flag indicating whether the ticket creation process should be stopped.
 *
 * The TicketCreator class provides the following methods:
 * - createTicketGroup(): Creates a Ticket group by taking user input for various attributes of the Ticket object.
 * - createTicketGroup(boolean isFromFile): Creates a Ticket group by generating random values for the attributes of the Ticket object.
 * - readName(String text): Reads and validates the name attribute of the Ticket object from user input.
 * - readNameWithNull(String text): Reads and validates the name attribute of the Ticket object from user input, allowing null values.
 * - readInteger(String text, int limit): Reads and validates an integer attribute of the Ticket object from user input.
 * - readIntegerWithNull(String text, int limit): Reads and validates an integer attribute of the Ticket object from user input, allowing null values.
 * - readLonger(String text, long limit): Reads and validates a long attribute of the Ticket object from user input.
 * - readLongerWithNull(String text): Reads and validates a long attribute of the Ticket object from user input, allowing null values.
 * - readTicketType(String text): Reads and validates the ticket type attribute of the Ticket object from user input.
 * - makeEvent(): Asks the user if an event should be created for the Ticket object and returns the user's choice.
 * - generateRandomString(): Generates a random string of characters for the name attribute of the Ticket object.
 * - generateRandomLong(int limit): Generates a random long value for the coordinates attribute of the Ticket object.
 * - generateRandomInt(int limit): Generates a random integer value for the price attribute of the Ticket object.
 * - generateRandomBoolean(): Generates a random boolean value for the event attribute of the Ticket object.
 * - generateRandomTicketType(): Generates a random TicketType value for the type attribute of the Ticket object.
 */

@Getter
public class TicketCreator {
    private Client client;
    private boolean isExit = false;
    public TicketCreator(Client client) {
        this.client = client;
    }

    public Ticket createTicketGroup() throws StopCreateTicketExceptionByClient {
        Ticket createdTicketGroup = new Ticket();
        createdTicketGroup.setName(readName("owner"));
        createdTicketGroup.setCoordinates(new Coordinates(
                readLonger("coordinates X", -503),
                readLonger("coordinates Y", -664)));
        createdTicketGroup.setCreationDate(new Date());
        createdTicketGroup.setPrice(readIntegerWithNull("price", 0));
        createdTicketGroup.setType(readTicketType(""));
        if (makeEvent()) {
            Event event = new Event(
                    readName("event"),
                    readLongerWithNull("event min age"),
                    readInteger("event tickets count", 0),
                    readNameWithNull("event description")
            );
            createdTicketGroup.setEvent(event);
        }
        return createdTicketGroup;
    }
    public Ticket createTicketGroup(boolean isFromFile) throws StopCreateTicketExceptionByClient {
        Ticket createdTicketGroup = new Ticket();
        createdTicketGroup.setName(generateRandomString());
        createdTicketGroup.setCoordinates(new Coordinates(
                generateRandomLong( -503),
                generateRandomLong(-664)));
        createdTicketGroup.setCreationDate(new Date());
        createdTicketGroup.setPrice(generateRandomInt(0));
        createdTicketGroup.setType(generateRandomTicketType());
        if (generateRandomBoolean()) {
            Event event = new Event(
                    generateRandomString(),
                    generateRandomLong(0),
                    generateRandomInt(0),
                    generateRandomString()
            );
            createdTicketGroup.setEvent(event);
        }
        return createdTicketGroup;
    }

    private String readName(String text) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " name:\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (!Validator.isValidString(str)) {
            client.getInputOutput().outPut("Incorrect string value\n");
            return readName(text);
        }
        return str;
    }

    private String readNameWithNull(String text) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " name:\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (Validator.isValidStringWithNull(str) == null) {
            return null;
        } else if (!Boolean.TRUE.equals(Validator.isValidStringWithNull(str))) {
            client.getInputOutput().outPut("Incorrect string value\n");
            return readNameWithNull(text);
        }
        return str;
    }

    private Integer readInteger(String text, int limit) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " int value (должно быть больше " + limit + "):\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (!Validator.isValidInteger(str, limit)) {
            client.getInputOutput().outPut("Incorrect int value\n");
            return readInteger(text, limit);
        }
        return Integer.parseInt(str);
    }

    private Integer readIntegerWithNull(String text, int limit) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " int value (должно быть больше " + limit + "):\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (Validator.isValidIntegerWithNull(str, limit) == null) {
            return null;
        } else if (!Boolean.TRUE.equals(Validator.isValidIntegerWithNull(str, limit))) {
            client.getInputOutput().outPut("Incorrect int value\n");
            return readIntegerWithNull(text, limit);
        }
        return Integer.parseInt(str);
    }


    private Long readLonger(String text, long limit) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " long value (должно быть больше " + limit + " ):\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (!Validator.isValidLonger(str, limit)) {
            client.getInputOutput().outPut("Incorrect long value\n");
            return readLonger(text, limit);
        }
        return Long.parseLong(str);
    }

    private Long readLongerWithNull(String text) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter " + text + " long value:\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if (Validator.isValidLongerWithNull(str) == null) {
            return null;
        } else if (!Boolean.TRUE.equals(Validator.isValidLongerWithNull(str))) {
            client.getInputOutput().outPut("Incorrect long value\n");
            return readLongerWithNull(text);
        }
        return Long.parseLong(str);
    }

    private TicketType readTicketType(String text) throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Enter" + text + " ticket type (VIP, USUAL, CHEAP):\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        if(!Validator.isValidTicketType(str)){
            client.getInputOutput().outPut("incorrect ticket type value\n");
            return readTicketType(text);
        }
        return TicketType.valueOf(str);
    }

    private boolean makeEvent() throws StopCreateTicketExceptionByClient {
        client.getInputOutput().outPut("Do you want to make event? (yes/no)\n~~ ");
        String str = client.getInputOutput().inPutConsole();
        if (str.equals("exit")){
            throw new StopCreateTicketExceptionByClient();
        }
        switch (str) {
            case "yes" -> {
                return true;
            }
            case "no" -> {
                return false;
            }
            default -> {
                return makeEvent();
            }
        }
    }


    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        int length = random.nextInt(1,10);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        if (!Validator.isValidString(randomString.toString())) {
            return generateRandomString();
        }
        return randomString.toString();
    }
    public long generateRandomLong(int limit) {
        Random random = new Random();
        long rnd = random.nextLong(-1000,1000);
        if(!Validator.isValidLonger(String.valueOf(rnd), limit)){
            return generateRandomLong(limit);
        }
        return rnd;
    }
    public int generateRandomInt(int limit){

        Random random = new Random();
        int rnd = random.nextInt(-1000,1000);
        if (!Validator.isValidInteger(String.valueOf(rnd), limit)) {
            return generateRandomInt(limit);
        }
        return rnd;
    }
    public boolean generateRandomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }
    public TicketType generateRandomTicketType(){
        Random random = new Random();
        int x = random.nextInt(0,3);
        switch (x){
            case 0 -> {
                return TicketType.VIP;
            }
            case 1 -> {
                return TicketType.USUAL;
            }
            case 2 -> {
                return TicketType.CHEAP;
            }
            default -> {
                return null;
            }
        }
    }
}
