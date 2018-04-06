package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import Data.DataHandler;
import Models.Cards.DestinationCard;
import Models.Command;
import Models.Gameplay.Game;
import Models.Gameplay.Player;
import Models.Gameplay.Route;
import Models.User;
import TestClient.TestClientServices;

public class Database {
    private HashMap<String, User> users; //Key: username, Value: user object
    private HashMap<String, Game> games; //Key: gameId, Value: game object
//    private ArrayList<String> clients; //List of active clients //For use with commands and polling mostly
    private HashSet<String> clients;
    private ArrayList <Command> masterCommandList;
//    private ArrayList <Route> routes;
    private HashMap<String, ArrayList<Command>> gameCommands;   //List of cmdObjects for each game
    private DataHandler dataHandler;
    private int cmdCount;
    private int delta = 1;

    private static Database theDB = new Database();

    public static Database getInstance() {
        return theDB;
    }

    private Database() {
        users = new HashMap<>();
        games = new HashMap<>();
//        clients = new ArrayList<>();
        clients = new HashSet<>();
        masterCommandList = new ArrayList<>();
//        routes = new ArrayList<>();
        dataHandler = new DataHandler();
        gameCommands = new HashMap<>();
        cmdCount = 0;
    }

    void loadTeam() {
        //        User u = new User("username","password","authToken");
        User u1 = new User("jordan", "aa", "a1fb6d30-51e7-4669-b944-120989aefb06");
        User u2 = new User("kip","aa","1fee61ae-d871-4548-8fba-a775dab78f8b");
        User u3 = new User("brian","aa","01b7cb2c-24c1-4c82-8f6f-c6ee8ab39d2e");
        User u4 = new User("finn","aa", "82f90744-ef61-4298-84ce-3070dfc25137");

        //add team users to database
        users.put(u1.getUsername(), u1);
        users.put(u1.getAuthToken(), u1);
        users.put(u2.getUsername(), u2);
        users.put(u2.getAuthToken(), u2);
        users.put(u3.getUsername(), u3);
        users.put(u3.getAuthToken(), u3);
        users.put(u4.getUsername(), u4);
        users.put(u4.getAuthToken(), u4);

        clients.add(u1.getAuthToken());
        clients.add(u2.getAuthToken());
        clients.add(u3.getAuthToken());
        clients.add(u4.getAuthToken());

        storeJsonUsers();

        TestClientServices.getInstance().createGame();
        storeJsonCMDs("lobby");
        storeJsonCMDs("game");
    }

    public HashMap<String, ArrayList<Command>> getAllGameCommands() {
        return gameCommands;
    }

    public ArrayList<Command> getGameCommands(String gameId) {
        return gameCommands.get(gameId);
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public ArrayList<DestinationCard> getDestinationCards() {
        return dataHandler.getDestinationCards();
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public Game getGameById(String id){
        return games.get(id);
    }

    public ArrayList<Player> getGamePlayers(String gameId) {
        return games.get(gameId).getPlayers();
    }

    public String findClientGame(String username) {
        for(Game g : games.values())
        {
            if(g.getPlayerNames().contains(username))
            {
                return g.getId();
            }
        }
        return "";
    }

    //We probably want to store it by token, but for login, we need to get it by username. We can do both
    private User findUserByToken(String token){
        return users.get(token);
    }

    public String getUsername(String token){
        return findUserByToken(token).getUsername();
    }

    public User findUserByName(String username) {
        return users.get(username);
    }

    public HashSet<String> getClients() {
        return clients;
    }

    public ArrayList<Command> getMasterCommandList() {
        return masterCommandList;
    }

    public void setMasterCommandList(ArrayList<Command> masterCommandList) {
        this.masterCommandList = masterCommandList;
        storeJsonCMDs("lobby");
    }

    public void addGameCommand(String gameId, Command command){

        if(gameCommands.containsKey(gameId)) {
            gameCommands.get(gameId).add(command);

        }else{

            ArrayList <Command> commands = new ArrayList<>();
            commands.add(command);
            gameCommands.put(gameId, commands);
        }

        storeJsonCMDs("game");

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonStr = gson.toJson(gameCommands);
//        String jsonGame = gson.toJson(games);
////        System.out.println(jsonStr);
//
//        try
//        {
//            PrintWriter out = new PrintWriter(new FileWriter("jsonCMDs.json"));
//            out.print(jsonStr);
//            out.close();
//
//            PrintWriter gameOut = new PrintWriter(new FileWriter("jsonGames.json"));
//            gameOut.print(jsonGame);
//            gameOut.close();
//
////            JsonReader cmdReader = new JsonReader(new FileReader("jsonCMDs.json"));
////            gameCommands = gson.fromJson(cmdReader, HashMap.class);
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    public void addGameHistory(String gameId, String msg){

        games.get(gameId).getHistory().addAction(msg);
    }

    public HashMap<Integer, Route> getRoutes() {
        return dataHandler.getRoutes();
    }

    public HashMap<Integer, Route> getDoubleRoutes() { return dataHandler.getDoubleRouteMap(); }



    void loadJSONdatabase() {
        loadJsonUsers();
        loadJsonGames();
        loadJsonCMDs();
    }

    public void storeJsonCMDs(String type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String filename = "";
        String jsonStr = "";
        if(type.equals("lobby"))
        {
            jsonStr = gson.toJson(masterCommandList);
            filename = "lobbyCommands.json";
        }
        else if(type.equals("game"))
        {
            jsonStr = gson.toJson(gameCommands);
            filename = "gameCommands.json";
        }

        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            out.print(jsonStr);
            out.close();

//            JsonReader cmdReader = new JsonReader(new FileReader("jsonCMDs.json"));
//            gameCommands = gson.fromJson(cmdReader, HashMap.class);
        }catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        cmdCount++;
        if(cmdCount == delta)
        {
            storeJsonGames();
            cmdCount = 0;
        }
    }

    public void loadJsonCMDs() {
        Gson gson = new GsonBuilder().create();
        Type typeGame = new TypeToken<HashMap<String, ArrayList<Command>>>(){}.getType();
        Type typeLobby = new TypeToken<ArrayList<Command>>(){}.getType();
        try
        {
            JsonReader cmdReader = new JsonReader(new FileReader("lobbyCommands.json"));
            masterCommandList = gson.fromJson(cmdReader, typeLobby);

            cmdReader = new JsonReader(new FileReader("gameCommands.json"));
            gameCommands = gson.fromJson(cmdReader, typeGame);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void storeJsonGames() { //stores entire hashmap of games
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonGames = gson.toJson(games);
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print(jsonGames);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadJsonGames() {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<HashMap<String, Game>>(){}.getType();
        try
        {
            JsonReader reader = new JsonReader(new FileReader("games.json"));
            games = gson.fromJson(reader, type);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void storeJsonUsers() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonUsers = gson.toJson(users);
        String jsonTokens = gson.toJson(clients);
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

    public void loadJsonUsers() {
        Gson gson = new GsonBuilder().create();
        Type typeUser = new TypeToken<HashMap<String, User>>(){}.getType();
        Type typeClients = new TypeToken<HashSet<String>>(){}.getType();

        try
        {
            JsonReader reader = new JsonReader(new FileReader("users.json"));
            users = gson.fromJson(reader, typeUser);

            reader = new JsonReader(new FileReader("activeClients.json"));
            clients = gson.fromJson(reader, typeClients);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void clearJsonData() {
        clearUsers();
        clearGames();
        clearCommands();
    }

    private void clearUsers() {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("users.json"));
            out.print("{}");
            out.close();

            out = new PrintWriter(new FileWriter("activeClients.json"));
            out.print("{}");
            out.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void clearGames() {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print("{}");
            out.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void clearCommands() {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("gameCommands.json"));
            out.print("{}");
            out.close();

            out = new PrintWriter(new FileWriter("lobbyCommands.json"));
            out.print("{}");
            out.close();


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
