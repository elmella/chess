package serverfacade;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    public static void main(String[] args) throws Exception {
        ServerFacade s = new ServerFacade();
        Map<String, String> headers = Map.of(
                "Authorization", "efc76910-0c03-424f-a639-048901c4b59f"
        );
        Object response = s.doGet("http://localhost:8080/game", headers);
        System.out.println(response);
    }

//    public void login();



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

    private static HttpURLConnection getHttpURLConnection(String urlString, String method, boolean doOutput,
                                                          Map<String, String> headers, String body) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(doOutput);

        // Set HTTP request headers, if necessary
        addHeaders(headers, connection);

        connection.connect();

        writeRequestBody(body, connection);
        return connection;
    }

    private static void writeRequestBody(String body, HttpURLConnection connection) throws IOException {
        if (!body.isEmpty()) {
            connection.setDoOutput(true);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static void addHeaders(Map<String,String> headers, HttpURLConnection connection) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
             connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static Object readResponseBody(HttpURLConnection connection) throws IOException {
        Object responseObject = "";
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                responseObject = new Gson().fromJson(inputStreamReader, Map.class);
            }
            return responseObject;
        } else {
            InputStream responseBody = connection.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
            return new Gson().fromJson(inputStreamReader, Map.class);
        }
    }
}
