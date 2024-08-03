package serverfacade;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private final Gson gson = new Gson();

    public Object clear(String baseURL) throws IOException {
        String route = "/db";
        String URL = baseURL + route;

        return doDelete(URL, null);
    }

    public Object register(String username, String password, String email, String baseURL) throws IOException {
        String route = "/user";
        String URL = baseURL + route;

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("username", username);
        bodyMap.put("password", password);
        bodyMap.put("email", email);

        String bodyJSON = gson.toJson(bodyMap, Map.class);

        return doPost(URL, null, bodyJSON);
    }

    public Object login(String username, String password, String baseURL) throws IOException {
        String route = "/session";
        String URL = baseURL + route;

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("username", username);
        bodyMap.put("password", password);

        String bodyJSON = gson.toJson(bodyMap, Map.class);

        return doPost(URL, null, bodyJSON);
    }

    public Object logout(String authToken, String baseURL) throws IOException {
        String route = "/session";
        String URL = baseURL + route;

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);

        return doDelete(URL, headers);
    }

    public Object listGames(String authToken, String baseURL) throws IOException {
        String route = "/game";
        String URL = baseURL + route;

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);

        return doGet(URL, headers);
    }

    public Object createGame(String gameName, String authToken, String baseURL) throws IOException {
        String route = "/game";
        String URL = baseURL + route;

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("gameName", gameName);

        String bodyJSON = gson.toJson(bodyMap, Map.class);

        return doPost(URL, headers, bodyJSON);
    }

    public Object joinGame(String playerColor, String gameID, String authToken, String baseURL) throws IOException {
        String route = "/game";
        String URL = baseURL + route;

        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("playerColor", playerColor);
        bodyMap.put("gameID", gameID);

        String bodyJSON = gson.toJson(bodyMap, Map.class);

        return doPut(URL, headers, bodyJSON);
    }




    private Object doGet(String urlString, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, "GET", false, headers, "");
        return readResponseBody(connection);
    }

    private Object doPost(String urlString, Map<String, String> headers, String body) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, "POST", true,  headers, body);
        return readResponseBody(connection);
    }

    private Object doPut(String urlString, Map<String, String> headers, String body) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, "PUT", true,  headers, body);
        return readResponseBody(connection);
    }

    private Object doDelete(String urlString, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlString, "DELETE", true,  headers, null);
        return readResponseBody(connection);
    }

    private HttpURLConnection getHttpURLConnection(String urlString, String method, boolean doOutput,
                                                          Map<String, String> headers, String body) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(doOutput);

        // Set HTTP request headers, if necessary
        if (headers != null) {
            addHeaders(headers, connection);
        }

        connection.connect();

        writeRequestBody(body, connection);
        return connection;
    }

    private void writeRequestBody(String body, HttpURLConnection connection) throws IOException {
        if (!body.isEmpty()) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private void addHeaders(Map<String,String> headers, HttpURLConnection connection) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
             connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private Object readResponseBody(HttpURLConnection connection) throws IOException {
        Object responseObject = "";
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                responseObject = gson.fromJson(inputStreamReader, Map.class);
            }
            return responseObject;
        } else {
            InputStream responseBody = connection.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
            return gson.fromJson(inputStreamReader, Map.class);
        }
    }
}
