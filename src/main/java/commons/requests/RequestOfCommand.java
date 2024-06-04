package commons.requests;


import commons.utilities.CommandValues;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
@Getter
public class RequestOfCommand implements Serializable {
    private String name;
    private CommandValues commandValues;
    private ArrayList<Object> params;
    private String login;
    private String password;
    public RequestOfCommand(String name, CommandValues commandValues, ArrayList<Object> params, String login, String password){
        this.name = name;
        this.commandValues = commandValues;
        this.params = params;
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestOfCommand{" +
                "name='" + name + '\'' +
                ", commandValues=" + commandValues +
                ", params=" + params +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
