package com.example;

import java.sql.SQLException;
import java.util.HashMap;


/**
 * Created by ferrell3 on 4/9/18.
 */

public interface GameDAO {
//    HashMap<String, Game> getGames();
//    void setGames(HashMap<String, Game> games);
//    void addGame(Game game);
//    void removeGame(Game game);
//    void loadGames();
//    void storeGames();

    void storeGames(String jsonStr) throws SQLException;
    String loadGames() throws SQLException;
    void clear() throws SQLException;
}
