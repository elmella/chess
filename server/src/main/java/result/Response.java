package result;

public class Response {

    /**
     * message : String
     */
    private String message;
    /**
     * success : boolean
     */
    private boolean success;

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

    public Response() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
