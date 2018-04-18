package Server;

import com.shared.CommandDAO;
import com.shared.GameDAO;
import com.shared.UserDAO;
import com.sql.commandDAO;
import com.sql.gameDAO;
import com.sql.userDAO;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

import Interfaces.PluginFactory;
import Plugin.JsonPluginFactory;
import Plugin.PluginDescriptor;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        PluginDescriptor pluginDescriptor = new PluginDescriptor();
        String portNumber = "8888";
        //do we want a default database too?
        String dbType = "";
        if(args.length > 0)
        {
            portNumber = args[0];
        }
        if(args.length > 1)
        {
            dbType = args[1];
        }
        Class c = null;
        Class u = null;
        Class g = null;
        CommandDAO commandDAO = null;
        GameDAO gameDAO = null;
        UserDAO userDAO = null;
        //Decide which Data Store to use:

//        String path = "";
//        if(dbType.equals("sql"))
//        {
//            path = "com.sql";
//        }
//        else //dbType.equals("json")
//        {
//            path = "com.json";
//        }
        try {
            c = Class.forName("com." + dbType + ".commandDAO");
            g = Class.forName("com." + dbType + ".gameDAO");
            u = Class.forName("com." + dbType + ".userDAO");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            commandDAO = (CommandDAO)c.newInstance();
            gameDAO = (GameDAO)g.newInstance();
            userDAO=(UserDAO)u.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        commandDAO = new commandDAO();
        gameDAO = new gameDAO();
        userDAO = new userDAO();
        //now you can use the implementation of this; though you may want it in another class


        new Server().init(portNumber);

    }

    private void init(String portNumber) {
        HttpServer server;

        System.out.println("Initializing HTTP server on port " + portNumber);

        //loads database with team hard coded users
//        Database.getInstance().loadTeam();
        Database.getInstance().loadJSONdatabase();

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
