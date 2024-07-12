package result;

public class Response {

    /**
     * success : boolean
     */
    private final boolean success;
    /**
     * message : String
     */
    private final String message;

    /**
     * constructor for Response
     *
     * @param message : String
     * @param success : boolean
     */
    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
