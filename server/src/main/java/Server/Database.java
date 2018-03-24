package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Data.DataHandler;
import Models.Cards.DestinationCard;
import Models.Command;
import Models.Gameplay.Game;
import Models.Gameplay.Player;
import Models.Gameplay.Route;
import Models.User;

public class Database {
    private HashMap<String, User> users; //Key: username, Value: user object
    private HashMap<String, Game> games; //Key: gameId, Value: game object
    private ArrayList<String> clients; //List of active clients //For use with commands and polling mostly
    private ArrayList <Command> masterCommandList;
//    private ArrayList <Route> routes;
    private HashMap<String, ArrayList<Command>> gameCommands;   //List of cmdObjects for each game
    private DataHandler dataHandler;

    private static Database theDB = new Database();

    public static Database getInstance() {
        return theDB;
    }

    private Database() {
        users = new HashMap<>();
        games = new HashMap<>();
        clients = new ArrayList<>();
        masterCommandList = new ArrayList<>();
//        routes = new ArrayList<>();
        dataHandler = new DataHandler();
        gameCommands = new HashMap<>();
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

    public ArrayList<String> getClients() {
        return clients;
    }

    public ArrayList<Command> getMasterCommandList() {
        return masterCommandList;
    }

    public void setMasterCommandList(ArrayList<Command> masterCommandList) {
        this.masterCommandList = masterCommandList;
    }

    public void addGameCommand(String gameId, Command command){

        if(gameCommands.containsKey(gameId)) {
            gameCommands.get(gameId).add(command);

        }else{

            ArrayList <Command> commands = new ArrayList<>();
            commands.add(command);
            gameCommands.put(gameId, commands);

        }
    }

    public void addGameHistory(String gameId, String msg){

        games.get(gameId).getHistory().addAction(msg);
    }

    public HashMap<Integer, Route> getRoutes() {
        return dataHandler.getRoutes();
    }
}
