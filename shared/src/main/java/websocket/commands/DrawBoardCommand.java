package websocket.commands;

public class DrawBoardCommand extends UserGameCommand {
    public DrawBoardCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}