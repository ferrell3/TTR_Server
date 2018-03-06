package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private List<String> gamePlays;

    public GameHistory(){
        gamePlays = new ArrayList<>();
    }

    public boolean addPlay(String play) {
        return gamePlays.add(play);
    }

    public void clearHistory() {
        gamePlays.clear();
    }

    public int size() {
        return gamePlays.size();
    }

    public String get(int i) {
        return gamePlays.get(i);
    }

    public String remove(int i) {
        return gamePlays.remove(i);
    }

    public boolean remove(String str) {
        return gamePlays.remove(str);
    }

    public int indexOf(String str){
        return gamePlays.indexOf(str);
    }

    public boolean isEmpty() {
        return gamePlays.isEmpty();
    }

    public List<String> getGamePlays() {
        return gamePlays;
    }

    public void setGamePlays(List<String> gamePlays) {
        this.gamePlays = gamePlays;
    }




}
