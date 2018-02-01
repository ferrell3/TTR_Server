package Server;

import java.util.HashMap;
import java.util.Map;
import Models.Game;
import Models.User;

public class Database {
    private HashMap<String, User> users;
    private HashMap<String, Game> games;

    private static Database theDB = new Database();

    public static Database getInstance() {
        return theDB;
    }
    private Database() {
        User u1 = new User("username","password","authToken");
        User u2 = new User("","",""); //Add constructor with params
        users.put(u1.getUsername(), u1);
        users.put(u1.getToken(), u1);
        users.put(u2.getUsername(), u2);
        users.put(u2.getToken(), u2);
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

    public User findUserByName(String username) {
        return users.get(username);
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
