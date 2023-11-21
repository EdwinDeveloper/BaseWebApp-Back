package ed.service.messaging.enums;

public enum UserTypes {

    //USER TYPES
    END_USER("END_USER"),
    INTEGRATOR_USER("INTEGRATOR_USER"),
    ADMIN_USER("ADMIN_USER");

    private final String userType;

    UserTypes(String type) {
        this.userType = type;
    }

    public String getType(){
        return userType;
    }
}
