package DAOs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

import Interfaces.UserDAO;
import Models.User;
import Server.Database;

public class JsonUserDAO implements UserDAO {

//    private HashMap<String, User> jsonUsers = new HashMap<>();
//    private HashSet<String> clients = new HashSet<>();
    @Override
    public HashMap<String, User> getUsers(){ //load users from json files
        Gson gson = new GsonBuilder().create();
        Type typeUser = new TypeToken<HashMap<String, User>>(){}.getType();
        Type typeClients = new TypeToken<HashSet<String>>(){}.getType();
        HashMap<String, User> jsonUsers = new HashMap<>();
//        HashSet<String> clients = new HashSet<>();
        try
        {
            JsonReader reader = new JsonReader(new FileReader("users.json"));
            jsonUsers = gson.fromJson(reader, typeUser);

            reader = new JsonReader(new FileReader("activeClients.json"));
            HashSet<String> clients = gson.fromJson(reader, typeClients);
            Database.getInstance().setClients(clients);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return jsonUsers;
    }

    @Override
    public void setUsers(HashMap<String, User> users) { //store users in json files
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUsers = gson.toJson(users);
        String jsonTokens = gson.toJson(Database.getInstance().getClients());
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("users.json"));
            out.print(jsonUsers);
            out.close();

            //if we restart the server, do we restart the clients too? If so, we don't need this
            //because the clients will have to re-login and thus they will be added to clients
            //but all the game info will be saved so they can jump right back into it either way
            out = new PrintWriter(new FileWriter("activeClients.json"));
            out.print(jsonTokens);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {}

    @Override
    public void removeUser(User user) {}

    @Override
    public void storeUsers() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUsers = gson.toJson(Database.getInstance().getUsers());
        String jsonTokens = gson.toJson(Database.getInstance().getClients());
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("users.json"));
            out.print(jsonUsers);
            out.close();

            //if we restart the server, do we restart the clients too? If so, we don't need this
            //because the clients will have to re-login and thus they will be added to clients
            //but all the game info will be saved so they can jump right back into it either way
            out = new PrintWriter(new FileWriter("activeClients.json"));
            out.print(jsonTokens);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUsers() {
        Gson gson = new GsonBuilder().create();
        Type typeUser = new TypeToken<HashMap<String, User>>(){}.getType();
        Type typeClients = new TypeToken<HashSet<String>>(){}.getType();
        try
        {
            JsonReader reader = new JsonReader(new FileReader("users.json"));
            HashMap<String, User> users = gson.fromJson(reader, typeUser);
            Database.getInstance().setUsers(users);

            reader = new JsonReader(new FileReader("activeClients.json"));
            HashSet<String> clients = gson.fromJson(reader, typeClients);
            Database.getInstance().setClients(clients);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
