package Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Models.Game;
import Models.User;

public class Database {
    private HashMap<String, User> users; //Key: username, Value: user object
    private HashMap<String, Game> games; //Key: gameId, Value: game object
    private List<String> clients; //List of active clients //For use with commands and polling mostly

    private static Database theDB = new Database();

    public static Database getInstance() {
        return theDB;
    }

    private Database() {
//        User u = new User("username","password","authToken");
        User u1 = new User("jordan", "jf", randomString());
        User u2 = new User("kip","kh",randomString());
        User u3 = new User("brian","bo",randomString());
        User u4 = new User("finn","fj",randomString());
        User u5 = new User("daniel","dk",randomString());

        //add team users to database
        users.put(u1.getUsername(), u1);
        users.put(u1.getAuthToken(), u1);
        users.put(u2.getUsername(), u2);
        users.put(u2.getAuthToken(), u2);
        users.put(u3.getUsername(), u3);
        users.put(u3.getAuthToken(), u3);
        users.put(u4.getUsername(), u4);
        users.put(u4.getAuthToken(), u4);
        users.put(u5.getUsername(), u5);
        users.put(u5.getAuthToken(), u5);
    }


    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGames(HashMap<String, Game> games) {
        this.games = games;
    }


    //We probably want to store it by token, but for login, we need to get it by username. We can do both
    public User findUserByToken(String token){
        return users.get(token);
    }

    public String getUsername(String token){
        return findUserByToken(token).getUsername();
    }

    public User findUserByName(String username) {
        return users.get(username);
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    //to create authTokens for hard coded team users
    public String randomString()
    {
        return UUID.randomUUID().toString();
    }


    //Not sure if we will use these or just getUsers().put(User) instead
//    public boolean addUser(User u){
//        return users.add(u);
//    }
//
//    public boolean removeUser(User u) {
//        return users.remove(u);
//    }
//
//    public boolean addGame(Game g){
//        return games.add(g);
//    }
//
//    public boolean removeGame(Game g){
//        return games.remove(g);
//    }

}
