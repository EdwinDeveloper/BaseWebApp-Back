package ed.service.messaging.dto;

import javax.persistence.Column;

public class UserDTO {

    private String firstName;
    private String lastNameFirst;
    private String lastNameSecond;
    private String email;

    private String type;

    private String password;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNameFirst() {
        return lastNameFirst;
    }

    public void setLastNameFirst(String lastNameFirst) {
        this.lastNameFirst = lastNameFirst;
    }

    public String getLastNameSecond() {
        return lastNameSecond;
    }

    public void setLastNameSecond(String lastNameSecond) {
        this.lastNameSecond = lastNameSecond;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
