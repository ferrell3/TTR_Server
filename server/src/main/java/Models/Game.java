package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Game {

    private String id;
    private List<String> players; //list of players' usernames
    private boolean joinable = true;

    public Game(){
        players = new ArrayList<>();
    }

    //constructor allowing to instantiate new game with given id
    public Game(String id){
        this.id = id;
        players = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public boolean isJoinable() {
        return joinable;
    }


    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }
}
