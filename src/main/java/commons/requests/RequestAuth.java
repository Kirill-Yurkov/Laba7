package commons.requests;

import lombok.Getter;

@Getter
public class RequestAuth extends Request {
    private final String login;
    private final String password;
    public RequestAuth(String login, String password){
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestAuth{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
