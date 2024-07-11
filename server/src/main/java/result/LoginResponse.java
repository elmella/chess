package result;

public class LoginResponse extends Response {

    /**
     * authToken : String
     */
    private String authToken;
    /**
     * userName : String
     */
    private String username;

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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
