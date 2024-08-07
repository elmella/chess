import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
import handler.UseGson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import websocket.commands.UserGameCommand;

@WebSocket
public class WSServer {
    private Gson gson;

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }
//
//    @OnWebSocketMessage
//    public void onMessage(Session session, String msg) {
//        try {
//            UserGameCommand command = UseGson.fromJson(message, UserGameCommand.class);
//
//            // Throws a custom UnauthorizedException. Yours may work differently.
//            String username = getUsername(command.getAuthString());
//
//            saveSession(command.getGameID(), session);
//
//            switch (command.getCommandType()) {
//                case CONNECT -> connect(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
//            }
//        } catch (UnauthorizedException ex) {
//            // Serializes and sends the error message
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
//        }
//    }

}