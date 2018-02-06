package Models;

import java.util.Map;

public class Game {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public Boolean isJoinable() {
        return joinable;
    }

    public void setJoinable(Boolean joinable) {
        this.joinable = joinable;
    }

    private String id;
    private Map<String,String> players;
    private Boolean joinable;


}
