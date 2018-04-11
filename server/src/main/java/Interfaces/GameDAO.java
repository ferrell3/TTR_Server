package Interfaces;

import java.util.HashMap;

import Models.Gameplay.Game;

/**
 * Created by ferrell3 on 4/9/18.
 */

public interface GameDAO {
    HashMap<String, Game> getGames();
    void setGames(HashMap<String, Game> games);
    void addGame(Game game);
    void removeGame(Game game);
    void loadGames();
    void storeGames();
}
