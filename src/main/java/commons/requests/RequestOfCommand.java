package commons.requests;


import commons.utilities.CommandValues;
import lombok.Getter;

import java.util.ArrayList;
@Getter
public class RequestOfCommand extends Request {
    private final String name;
    private final CommandValues commandValues;
    private final ArrayList<Object> params;
    private final String login;
    private final String password;
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
