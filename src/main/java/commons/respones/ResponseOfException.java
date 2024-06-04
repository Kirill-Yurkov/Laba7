package commons.respones;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResponseOfException implements Serializable {
    private String name;
    private Exception exception ;
    public ResponseOfException(String name, Exception exception){
        this.name = name;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ResponseOfCommand{" +
                "name='" + name + '\'' +
                ", answer='" + exception + '\'' +
                '}';
    }
}
