package Server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;

    public static void main(String[] args) {
        String portNumber = "8888";
        new Server().init(portNumber);
    }

    private void init(String portNumber) {
        HttpServer server;

        System.out.println("Initializing HTTP server on port " + portNumber);

        //loads database with team hard coded users
        Database.getInstance().loadTeam();

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");
        server.createContext("/", new Handler());
        System.out.println("Starting server");

        server.start();
        System.out.println("Server started");

    }
}
