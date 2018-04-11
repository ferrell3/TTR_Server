package DAOs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

import Interfaces.GameDAO;
import Models.Gameplay.Game;
import Server.Database;

public class JsonGameDAO implements GameDAO {
    @Override
    public HashMap<String, Game> getGames() { //load games from json files
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<HashMap<String, Game>>(){}.getType();
        HashMap<String, Game> jsonGames = new HashMap<>();
        try
        {
            JsonReader reader = new JsonReader(new FileReader("games.json"));
            jsonGames = gson.fromJson(reader, type);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return jsonGames;
    }

    @Override
    public void setGames(HashMap<String, Game> games) { //save games in json files
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonGames = gson.toJson(games);
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print(jsonGames);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addGame(Game game) {
    }

    @Override
    public void removeGame(Game game) {
    }

    @Override
    public void storeGames() { //stores entire hashmap of games
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonGames = gson.toJson(Database.getInstance().getGames());
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print(jsonGames);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void loadGames() {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<HashMap<String, Game>>(){}.getType();
        try
        {
            JsonReader reader = new JsonReader(new FileReader("games.json"));
            HashMap<String, Game> games = gson.fromJson(reader, type);
            Database.getInstance().setGames(games);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
