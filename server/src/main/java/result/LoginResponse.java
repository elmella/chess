package result;

public class LoginResponse extends Response {

    /**
     * authToken : String
     */
    private final String authToken;
    /**
     * userName : String
     */
    private final String username;

    /**
     * constructor for LoginResponse for a successful login
     *
     * @param authToken : String
     * @param username  : String
     */
    public LoginResponse(String authToken, String username, boolean success, String message) {
        super(message, success);
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
