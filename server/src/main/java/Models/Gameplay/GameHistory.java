package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private List<String> gameActions;

    public GameHistory(){
        gameActions = new ArrayList<>();
    }

    public boolean addAction(String play) {
        return gameActions.add(play);
    }

    public void clearHistory() {
        gameActions.clear();
    }

    public int size() {
        return gameActions.size();
    }

    public String get(int i) {
        return gameActions.get(i);
    }

    public String remove(int i) {
        return gameActions.remove(i);
    }

    public boolean remove(String str) {
        return gameActions.remove(str);
    }

    public int indexOf(String str){
        return gameActions.indexOf(str);
    }

    public boolean isEmpty() {
        return gameActions.isEmpty();
    }

    public List<String> getGameActions() {
        return gameActions;
    }

    public void setGameActions(List<String> gameActions) {
        this.gameActions = gameActions;
    }




}
