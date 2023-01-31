package cz.kratochvil.knihovna;

import java.io.Serializable;

public class User implements Serializable {

    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return username + " " + password;
    }
}
