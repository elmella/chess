package websocket.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import websocket.commands.*;

public class MessageSerializer {

    public static Gson createSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(ServerMessage.class,
                (JsonDeserializer<ServerMessage>) (el, type, ctx) -> {
                    ServerMessage serverMessage = null;
                    if (el.isJsonObject()) {
                        String serverMessageType = el.getAsJsonObject().get("serverMessageType").getAsString();
                        switch (ServerMessage.ServerMessageType.valueOf(serverMessageType)) {
                            case LOAD_GAME -> serverMessage = ctx.deserialize(el, LoadGameMessage.class);
                            case NOTIFICATION -> serverMessage = ctx.deserialize(el, NotificationMessage.class);
                            case ERROR -> serverMessage = ctx.deserialize(el, ErrorMessage.class);
                        }
                    }
                    return serverMessage;
                });

        return gsonBuilder.create();
    }
}
