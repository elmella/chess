public class ClientMain {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8080";
        if (args.length == 1) {
            baseUrl = args[0];
        }

        new Navigate(baseUrl).run();

    }
}


