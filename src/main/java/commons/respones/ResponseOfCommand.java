package commons.respones;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResponseOfCommand implements Serializable {
    private String name;
    private String answer;

    public ResponseOfCommand(String name, String answer) {
        this.name = name;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "ResponseOfCommand{" +
                "name='" + name + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
