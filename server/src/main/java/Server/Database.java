package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shared.CommandDAO;
import com.shared.GameDAO;
import com.shared.UserDAO;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Data.DataHandler;
import Interfaces.PluginFactory;
import Models.Cards.DestinationCard;
import Models.Command;
import Models.Gameplay.Game;
import Models.Gameplay.Player;
import Models.Gameplay.Route;
import Models.User;
import Plugin.JsonPluginFactory;
import Plugin.SqlPluginFactory;

public class Database {
    private HashMap<String, User> users; //Key: username, Value: user object
    private HashMap<String, Game> games; //Key: gameId, Value: game object
//    private ArrayList<String> clients; //List of active clients //For use with commands and polling mostly
//    private HashSet<String> clients;
    private ArrayList <Command> masterCommandList;
//    private ArrayList <Route> routes;
    private HashMap<String, ArrayList<Command>> gameCommands;   //List of cmdObjects for each game
    private DataHandler dataHandler;
    private int cmdCount;

    private int delta;
    private CommandDAO commandDAO = null;
    private GameDAO gameDAO = null;
    private UserDAO userDAO = null;

    private static Database theDB = new Database();

    public static Database getInstance() {
        return theDB;
    }

    private Database() {
        users = new HashMap<>();
        games = new HashMap<>();
//        clients = new ArrayList<>();
//        clients = new HashSet<>();
        masterCommandList = new ArrayList<>();
//        routes = new ArrayList<>();
        dataHandler = new DataHandler();
        gameCommands = new HashMap<>();
        cmdCount = 0;
    }

//    void loadTeam() {
//        //        User u = new User("username","password","authToken");
//        User u1 = new User("jordan", "aa", "a1fb6d30-51e7-4669-b944-120989aefb06");
//        User u2 = new User("kip","aa","1fee61ae-d871-4548-8fba-a775dab78f8b");
//        User u3 = new User("brian","aa","01b7cb2c-24c1-4c82-8f6f-c6ee8ab39d2e");
//        User u4 = new User("finn","aa", "82f90744-ef61-4298-84ce-3070dfc25137");
//
//        //add team users to database
//        users.put(u1.getUsername(), u1);
//        users.put(u1.getAuthToken(), u1);
//        users.put(u2.getUsername(), u2);
//        users.put(u2.getAuthToken(), u2);
//        users.put(u3.getUsername(), u3);
//        users.put(u3.getAuthToken(), u3);
//        users.put(u4.getUsername(), u4);
//        users.put(u4.getAuthToken(), u4);
//
////        clients.add(u1.getAuthToken());
////        clients.add(u2.getAuthToken());
////        clients.add(u3.getAuthToken());
////        clients.add(u4.getAuthToken());
//
////        storeUsers();
//        TestClientServices.getInstance().createGame();
////        storeJsonCMDs("lobby");
////        storeJsonCMDs("game");
//    }

    public HashMap<String, ArrayList<Command>> getAllGameCommands() {
        return gameCommands;
    }

    public ArrayList<Command> getGameCommands(String gameId) {
        return gameCommands.get(gameId);
    }

    public void setGameCommands(HashMap<String, ArrayList<Command>> gameCommands) {
        this.gameCommands = gameCommands;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public ArrayList<DestinationCard> getDestinationCards() {
        return dataHandler.getDestinationCards();
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGames(HashMap<String, Game> games) {
        this.games = games;
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

//    public HashSet<String> getClients() {
//        return clients;
//    }
//
//    public void setClients(HashSet<String> clients) {
//        this.clients = clients;
//    }

    public ArrayList<Command> getMasterCommandList() {
        return masterCommandList;
    }

    public void setMasterCommandList(ArrayList<Command> masterCommandList) {
        this.masterCommandList = masterCommandList;
        storeLobbyCommands();
    }

    public void addGameCommand(String gameId, Command command){

        if(gameCommands.containsKey(gameId)) {
            gameCommands.get(gameId).add(command);

        }else{

            ArrayList <Command> commands = new ArrayList<>();
            commands.add(command);
            gameCommands.put(gameId, commands);
        }

        storeGameCommands();
    }

    public void addGameHistory(String gameId, String msg){

        games.get(gameId).getHistory().addAction(msg);
    }

    public HashMap<Integer, Route> getRoutes() {
        return dataHandler.getRoutes();
    }

    public HashMap<Integer, Route> getDoubleRoutes() { return dataHandler.getDoubleRouteMap(); }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public void registerPlugin(String dbType) {
        PluginFactory piFactory;

        Class c = null;
        Class u = null;
        Class g = null;

        //Decide which Data Store to use:

        String path = "";
        if(dbType.equals("sql"))
        {
            path = "com.sql.sql";
            piFactory = new SqlPluginFactory();
        }
        else //dbType.equals("json")
        {
            path = "com.json.json";
            piFactory = new JsonPluginFactory();
        }
        try {
            c = Class.forName(path + "CommandDAO");
            g = Class.forName(path + "GameDAO");
            u = Class.forName(path + "UserDAO");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            commandDAO = (CommandDAO) c.newInstance();
            gameDAO = (GameDAO) g.newInstance();
            userDAO = (UserDAO) u.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        commandDAO = piFactory.createCommandDAO();
        gameDAO = piFactory.createGameDAO();
        userDAO = piFactory.createUserDAO();
        loadDatabase();
    }

    private void loadDatabase() {
        try
        {
            deserializeUsers(userDAO.loadUsers());
            deserializeCommands(commandDAO.loadLobbyCommands(), commandDAO.loadGameCommands());
            deserializeGames(gameDAO.loadGames());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

//        for(int i = 0; i < gameCommands.size(); i++)
        for(ArrayList<Command> cmdList : gameCommands.values())
        {
            for(Command cmd : cmdList)
            {
                //TODO: fix me
                cmd.execute();
            }
        }
    }

    public void storeUsers() {
        try
        {
            userDAO.storeUsers(serializeUsers());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeGames() {
        try
        {
            gameDAO.storeGames(serializeGames());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeLobbyCommands() {
        try
        {
            commandDAO.storeLobbyCommands(serializeLobbyCommands());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void storeGameCommands() {
        try
        {
            commandDAO.storeGameCommands(serializeGameCommands());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        cmdCount++;
        if(cmdCount == delta)
        {
            storeGames();
            cmdCount = 0;
            System.out.println("Games stored!");
            try
            {
                commandDAO.clearGameCommands();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void clear() {
        try
        {
            userDAO.clear();
            gameDAO.clear();
            commandDAO.clear();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    void loadJSONdatabase() {
//        JsonUserDAO juDAO = new JsonUserDAO();
//        JsonCommandDAO jcDAO = new JsonCommandDAO();
//        JsonGameDAO jgDAO = new JsonGameDAO();
//
//        deserializeUsers(juDAO.loadUsers());
//        deserializeGames(jgDAO.loadGames());
//        deserializeCommands(jcDAO.loadLobbyCommands(), jcDAO.loadGameCommands());
//    }

//    public void storeJsonCMDs(String type) {
//        JsonCommandDAO jcDAO = new JsonCommandDAO();
//        if(type.equals("lobby"))
//        {
//            jcDAO.storeLobbyCommands(serializeLobbyCommands());
//        }
//        else if(type.equals("game"))
//        {
//            jcDAO.storeGameCommands(serializeGameCommands());
//        }
//        cmdCount++;
//        if(cmdCount == delta)
//        {
//            storeGames();
//            cmdCount = 0;
//        }
//    }

//    public void storeGames() { //stores entire hashmap of games
//        JsonGameDAO jgDAO = new JsonGameDAO();
////        jgDAO.setGames(games);
//        jgDAO.storeGames(serializeGames());
//    }

//    public void storeUsers() {
//        JsonUserDAO juDAO = new JsonUserDAO();
////        juDAO.setUsers(users);
//        juDAO.storeUsers(serializeUsers());
//    }


//    public void clearJsonData() {
//        new JsonUserDAO().clear();
//        new JsonGameDAO().clear();
//        new JsonCommandDAO().clear();
//    }

    private String serializeUsers() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(users);
    }

    private void deserializeUsers(String jsonStr){
        Gson gson = new GsonBuilder().create();
        Type typeUser = new TypeToken<HashMap<String, User>>(){}.getType();
//        Type typeClients = new TypeToken<HashSet<String>>(){}.getType();
        users = gson.fromJson(jsonStr, typeUser);
        if(users == null)
        {
            users = new HashMap<>();
        }
    }

    private String serializeGames() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(games);
    }

    private void deserializeGames(String jsonStr){
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<HashMap<String, Game>>(){}.getType();
        games = gson.fromJson(jsonStr, type);
        if(games == null)
        {
            games = new HashMap<>();
        }
    }

    private String serializeLobbyCommands() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(masterCommandList);
    }

    private String serializeGameCommands() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gameCommands);
    }

    private void deserializeCommands(String jsonLobbyCommands, String jsonGameCommands){
        Gson gson = new GsonBuilder().create();
        Type typeLobby = new TypeToken<ArrayList<Command>>(){}.getType();
        Type typeGame = new TypeToken<HashMap<String, ArrayList<Command>>>(){}.getType();

        masterCommandList = gson.fromJson(jsonLobbyCommands, typeLobby);
        gameCommands = gson.fromJson(jsonGameCommands, typeGame);
        if(masterCommandList == null)
        {
            masterCommandList = new ArrayList<>();
        }
        if(gameCommands == null)
        {
            gameCommands = new HashMap<>();
        }
    }
}