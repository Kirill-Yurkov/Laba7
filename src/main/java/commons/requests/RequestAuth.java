package commons.requests;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class RequestAuth implements Serializable {
    private String login;
    private String password;
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
