package serverfacade;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.ServerMessageHandler;
import websocket.commands.*;
import websocket.exception.ResponseException;
import websocket.messages.MessageSerializer;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    private final Gson commandGson = CommandSerializer.createSerializer();
    private final Gson messageGson = MessageSerializer.createSerializer();
    Session session;
    ServerMessageHandler serverMessageHandler;

    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = messageGson.fromJson(message, ServerMessage.class);
                    serverMessageHandler.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void connect(String authToken, int gameID) throws IOException {
        ConnectCommand command = new ConnectCommand(UserGameCommand.CommandType.CONNECT,
                authToken, gameID);

        String jsonCommand = commandGson.toJson(command);
        send(jsonCommand);
    }

    public void makeMove(int startRow, String startCol, int endRow, String endCol, String piece,
                         String authToken, int gameID) throws IOException {
        Map<String, Integer> colInt = Map.of(
                "a", 1,
                "b", 2,
                "c", 3,
                "d", 4,
                "e", 5,
                "f", 6,
                "g", 7,
                "h", 8
        );

        // String to PieceType map
        ChessPiece.PieceType promotionPiece = (piece == null) ? null : stringToPieceType(piece);

        // Create chess positions
        ChessPosition startPos = new ChessPosition(startRow, colInt.get(startCol));
        ChessPosition endPos = new ChessPosition(endRow, colInt.get(endCol));

        // Create chess move
        ChessMove move = new ChessMove(startPos, endPos, promotionPiece);

        // Create MakeMoveCommand
        MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                authToken, gameID, move);

        String jsonCommand = commandGson.toJson(command);
        send(jsonCommand);
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        LeaveGameCommand command = new LeaveGameCommand(UserGameCommand.CommandType.LEAVE,
                authToken, gameID);

        String jsonCommand = commandGson.toJson(command);
        send(jsonCommand);
    }

    public void resign(String authToken, int gameID) throws IOException {
        ResignCommand command = new ResignCommand(UserGameCommand.CommandType.RESIGN,
                authToken, gameID);

        String jsonCommand = commandGson.toJson(command);
        send(jsonCommand);
    }

    public void drawBoard(String authToken, int gameID) throws IOException {
        DrawBoardCommand command = new DrawBoardCommand(UserGameCommand.CommandType.DRAW_BOARD,
                authToken, gameID);

        String jsonCommand = commandGson.toJson(command);
        send(jsonCommand);
    }

    public void send(String msg) throws IOException {
        if (this.session.isOpen()) {
            this.session.getBasicRemote().sendText(msg);
        } else {
            System.out.println("Session is not open, please leave and rejoin.");
        }
    }

            //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private ChessPiece.PieceType stringToPieceType(String pieceString) {
        Map<String, ChessPiece.PieceType> pieceMap = Map.of(
                "KING", ChessPiece.PieceType.KING,
                "QUEEN", ChessPiece.PieceType.QUEEN,
                "ROOK", ChessPiece.PieceType.ROOK,
                "BISHOP", ChessPiece.PieceType.BISHOP,
                "KNIGHT", ChessPiece.PieceType.KNIGHT,
                "PAWN", ChessPiece.PieceType.PAWN
        );
        return pieceMap.get(pieceString);
    }
}