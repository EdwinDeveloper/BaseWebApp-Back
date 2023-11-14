package ed.service.messaging.dto;

import ed.service.messaging.entity.jpa.User;

public class UserDTOR {

    private String id;
    private String firstName;
    private String lastNameFirst;
    private String lastNameSecond;
    private String email;

    public UserDTOR(){}

    public UserDTOR(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastNameFirst = user.getLastNameFirst();
        this.lastNameSecond = user.getLastNameSecond();
        this.email = user.getEmail();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

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

}
