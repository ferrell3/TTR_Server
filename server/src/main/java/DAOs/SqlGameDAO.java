package DAOs;

import java.util.HashMap;

import Interfaces.GameDAO;
import Models.Gameplay.Game;

public class SqlGameDAO implements GameDAO {
    @Override
    public void storeGames(String jsonStr) {

    }

    @Override
    public String loadGames() { //directly into the database (i.e. Database.getInstance().setGames(...);
        return null;
    }

    @Override
    public void clear() {

    }

//    @Override
//    public HashMap<String, Game> getGames() {
//        return null;
//    }
//
//    @Override
//    public void setGames(HashMap<String, Game> games) {
//
//    }
//
//    @Override
//    public void addGame(Game game) {
//
//    }
//
//    @Override
//    public void removeGame(Game game) {
//
//    }
//
//    @Override
//    public void storeGames() {
//
//    }
}
