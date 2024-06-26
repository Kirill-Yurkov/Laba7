package commons.responses;


import lombok.Getter;

@Getter
public class ResponseOfCommand extends Response {
    private final String name;
    private final String answer;

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
