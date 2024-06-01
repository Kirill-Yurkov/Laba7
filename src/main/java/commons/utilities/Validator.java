package commons.utilities;

import commons.patternclass.TicketType;

/**
 * The Validator class provides utility methods for validating strings, integers, longs, and ticket types.
 *
 * This class cannot be instantiated as it only contains static methods.
 *
 * The methods in this class can be used to check if a string is valid, if an integer is valid within a specified limit,
 * if a long is valid, and if a ticket type is valid.
 *
 * The class provides methods for checking if a string is empty, blank, or equal to "null". It also provides methods for
 * checking if a string is a valid integer or long, and if a string represents a valid ticket type.
 *
 * The methods return a boolean value indicating whether the input is valid or not. Some methods also return a Boolean
 * value, which can be null in case the input is equal to "null".
 *
 * Example usage:
 *
 * boolean isValid = Validator.isValidString("example");
 * boolean isValidInteger = Validator.isValidInteger("123", 100);
 * boolean isValidLong = Validator.isValidLonger("1000", 500);
 * boolean isValidTicketType = Validator.isValidTicketType("VIP");
 *
 * Note: This class assumes that the TicketType enum is imported and available in the classpath.
 */
public class Validator {
    private Validator(){}
    public static boolean isValidString(String str) {
        if (str.isEmpty() || str.isBlank() || str.equals("null")) {
            return false;
        }
        return true;
    }

    public static Boolean isValidStringWithNull(String str) {
        if (str.equals("null")) {
            return null;
        }
        if (str.isEmpty() || str.isBlank()) {
            return false;
        }
        return true;
    }

    public static boolean isValidInteger(String strInteger, int limit) {
        try {
            if (strInteger.isEmpty() || strInteger.isBlank() || Integer.parseInt(strInteger) <= limit) {
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
    public static boolean isValidInteger(String strInteger) {
        try {
            if (strInteger.isEmpty() || strInteger.isBlank()) {
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static Boolean isValidIntegerWithNull(String strInteger, int limit) {
        try {
            if (strInteger.equals("null")) {
                return null;
            }
            if (strInteger.isEmpty() || strInteger.isBlank() || Integer.parseInt(strInteger) <= limit) {
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isValidLonger(String strLonger, long limit) {
        try {
            if (strLonger.isEmpty() || strLonger.isBlank() || Long.parseLong(strLonger) <= limit) {
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
    public static boolean isValidLonger(String strLonger) {
        try {
            if (strLonger.isEmpty() || strLonger.isBlank()) {
                return false;
            }
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
    public static Boolean isValidLongerWithNull(String strLonger) {
        if (strLonger.equals("null")) {
            return null;
        }
        if (strLonger.isEmpty() || strLonger.isBlank()) {
            return false;
        }
        try {
            Long.parseLong(strLonger);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isValidTicketType(String strTicketType){
        if (strTicketType.isEmpty() || strTicketType.isBlank()) {
            return false;
        }
        try {
            TicketType.valueOf(strTicketType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
   /*
   public static boolean isValidTicket(Ticket ticket){
        if(!isValidString(ticket.getName())){
            return false;
        }
        if(!isValidLonger(String.valueOf(ticket.getCoordinates().getX()), -503)){
            return false;
        }
        if(!isValidLonger(String.valueOf(ticket.getCoordinates().getY()), -664)){
            return false;
        }
        if(ticket.getCreationDate() == null){
            return false;
        }
        if(Boolean.FALSE.equals(isValidIntegerWithNull(String.valueOf(ticket.getPrice()), 0))){
            return false;
        }
        if(!isValidTicketType(String.valueOf(ticket.getType()))){
            return false;
        }
        if(ticket.getEvent()!=null){
            if(!isValidString(ticket.getEvent().getName())){
                return false;
            }
            if(Boolean.FALSE.equals(isValidLongerWithNull(String.valueOf(ticket.getEvent().getMinAge())))){
                return false;
            }
            if(!isValidInteger(String.valueOf(ticket.getEvent().getTicketsCount()),0)){
                return false;
            }
            if(Boolean.FALSE.equals(isValidStringWithNull(ticket.getEvent().getDescription()))){
                return false;
            }
        }
        return true;
    }

    private static boolean isValidResponseValue(Object value){
        return ((value instanceof Integer) && (isValidInteger(value.toString()))) || ((value instanceof Long) && (isValidLonger(value.toString())));
    }
    private static boolean isValidResponseElement(Object value){
        return ((value instanceof Ticket) && (isValidTicket((Ticket) value)) || ((value instanceof client.patternclass.TicketType) && (isValidTicketType(value.toString()))));
    }
    public static boolean isValidResponse(Response response){
        switch (response.getCommandValues()){
            case NOTHING -> {
                return response.getParams().isEmpty();
            }
            case VALUE -> {
                if(response.getParams().size()==1){
                    return  isValidResponseValue(response.getParams().get(0));
                } else {
                    return false;
                }
            }
            case ELEMENT -> {
                if(response.getParams().size()==1){
                    return isValidResponseElement(response.getParams().get(0));
                } else{
                    return false;
                }
            }
            case VALUE_ELEMENT -> {
                if(response.getParams().size()==2){
                    return (isValidResponseElement(response.getParams().get(0)) && isValidResponseValue(response.getParams().get(1))) || (isValidResponseElement(response.getParams().get(1)) && isValidResponseValue(response.getParams().get(0)));
                } else{
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }
    */
}
