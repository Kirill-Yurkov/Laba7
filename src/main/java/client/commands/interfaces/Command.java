package client.commands.interfaces;

import client.Client;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.utilities.CommandValues;
import commons.requests.RequestOfCommand;

public interface Command {
    CommandValues getValue();
    void setClient(Client client);
    RequestOfCommand makeRequest(String value) throws CommandValueException, CommandCollectionZeroException;
    //String executeResponse(String answer) throws BadResponseException;
    String getName();
    String description();
}
