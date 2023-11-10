package ed.service.messaging.enums;

public enum UserTypes {

    //USER TYPES
    END_USER("END_USER"),
    INTEGRATOR_USER("INTEGRATOR_USER"),
    ADMIN_USER("ADMIN_USER");

    private final String type;

    UserTypes(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
