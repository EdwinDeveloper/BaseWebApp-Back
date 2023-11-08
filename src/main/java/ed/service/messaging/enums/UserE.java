package ed.service.messaging.enums;

public enum UserE {
    ACTIVE("ACTIVE");

    private final String status;

    UserE(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
