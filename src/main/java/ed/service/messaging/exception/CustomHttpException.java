package ed.service.messaging.exception;

public class CustomHttpException extends Exception{

    private int serverExceptionCode = 0;
    private String serverExceptionMessage = "";

    public CustomHttpException(String error){ super(error); }

    public CustomHttpException( int serverExceptionCode, String serverExceptionMessage){
        super(serverExceptionMessage);
        this.serverExceptionCode = serverExceptionCode;
        this.serverExceptionMessage = serverExceptionMessage;
    }

    public int getServerExceptionCode(){
        return serverExceptionCode;
    }

    public String getServerExceptionMessage(){
        return serverExceptionMessage;
    }

}
