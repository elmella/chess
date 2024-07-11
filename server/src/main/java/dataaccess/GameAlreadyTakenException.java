package dataaccess;

public class GameAlreadyTakenException extends GameServiceException {
    public GameAlreadyTakenException(String message) {
        super(message);
    }
}
