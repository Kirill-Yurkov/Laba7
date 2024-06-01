package commons.utilities;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class Response implements Serializable {
    private String name;
    private String answer;

    public Response(String name, String answer) {
        this.name = name;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Response{" +
                "name='" + name + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
