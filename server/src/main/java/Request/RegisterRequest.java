package Request;

public record RegisterRequest(
        String username,
        String email,
        String password){
}