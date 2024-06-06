package commons.responses;


import lombok.Getter;

@Getter
public class ResponseOfException extends Response {
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
