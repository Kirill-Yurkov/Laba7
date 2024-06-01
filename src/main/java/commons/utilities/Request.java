package commons.utilities;


import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
@Getter
public class Request implements Serializable {
    private String name;
    private CommandValues commandValues;
    private ArrayList<Object> params;
    public Request(String name, CommandValues commandValues, ArrayList<Object> params){
        this.name = name;
        this.commandValues = commandValues;
        this.params = params;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", commandValues=" + commandValues +
                ", params=" + params +
                '}';
    }
}
