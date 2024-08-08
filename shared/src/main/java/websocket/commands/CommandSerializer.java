package websocket.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

public class CommandSerializer {

    public static Gson createSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(UserGameCommand.class,
                (JsonDeserializer<UserGameCommand>) (el, type, ctx) -> {
                    UserGameCommand userGameCommand = null;
                    if (el.isJsonObject()) {
                        String commandType = el.getAsJsonObject().get("commandType").getAsString();
                        switch (UserGameCommand.CommandType.valueOf(commandType)) {
                            case CONNECT -> userGameCommand = ctx.deserialize(el, ConnectCommand.class);
                            case MAKE_MOVE -> userGameCommand = ctx.deserialize(el, MakeMoveCommand.class);
                            case LEAVE -> userGameCommand = ctx.deserialize(el, LeaveGameCommand.class);
                            case RESIGN -> userGameCommand = ctx.deserialize(el, ResignCommand.class);
                        }
                    }
                    return userGameCommand;
                });

        return gsonBuilder.create();
    }
}
