package handler;

import com.google.gson.Gson;

import request.Request;
import result.Response;

import java.lang.reflect.Type;

public class UseGson {


    public static Request fromJson(String json, Type t) {
        Gson serializer = new Gson();
        return serializer.fromJson(json, t);
    }

    public static String toJson(Object o) {
        Gson serializer = new Gson();
        return serializer.toJson(o);
    }

//    public static Gson createSerializer() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        gsonBuilder.registerTypeAdapter(ChessPiece.class,
//                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> {
//                    ChessPiece chessPiece = null;
//                    if (el.isJsonObject()) {
//                        String pieceType = el.getAsJsonObject().get("type").getAsString();
//                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
//                            case PAWN -> chessPiece = ctx.deserialize(el, Pawn.class);
//                            case ROOK -> chessPiece = ctx.deserialize(el, Rook.class);
//                            case KNIGHT -> chessPiece = ctx.deserialize(el, Knight.class);
//                            case BISHOP -> chessPiece = ctx.deserialize(el, Bishop.class);
//                            case QUEEN -> chessPiece = ctx.deserialize(el, Queen.class);
//                            case KING -> chessPiece = ctx.deserialize(el, King.class);
//                        }
//                    }
//                    return chessPiece;
//                });

//        return gsonBuilder.create();
}