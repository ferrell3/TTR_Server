package DAOs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.CommandDAO;
import Models.Command;
import Server.Database;

public class JsonCommandDAO implements CommandDAO {

    //delta and cmdCount?

    @Override
    public HashMap<String, ArrayList<Command>> getGameCommands() {
        Gson gson = new GsonBuilder().create();
        Type typeGame = new TypeToken<HashMap<String, ArrayList<Command>>>(){}.getType();
        try
        {
            JsonReader cmdReader = new JsonReader(new FileReader("gameCommands.json"));
            return gson.fromJson(cmdReader, typeGame);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setGameCommands(HashMap<String, ArrayList<Command>> commands) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(commands);
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("gameCommands.json"));
            out.print(jsonStr);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addCommand(Command cmd) {

    }

    @Override
    public void removeCommand(Command cmd) {

    }

    @Override
    public void storeGameCommands() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(Database.getInstance().getAllGameCommands());
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("gameCommands.json"));
            out.print(jsonStr);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
//            return;
        }

//        cmdCount++;
//        if(cmdCount == delta)
//        {
//            storeJsonGames();
//            cmdCount = 0;
//        }
    }

//    public void loadGameCommands() {
//        Gson gson = new GsonBuilder().create();
//        Type typeGame = new TypeToken<HashMap<String, ArrayList<Command>>>(){}.getType();
//        try
//        {
//            JsonReader cmdReader = new JsonReader(new FileReader("gameCommands.json"));
//            HashMap<String, ArrayList<Command>> gameCommands = gson.fromJson(cmdReader, typeGame);
//            Database.getInstance().setGameCommands(gameCommands);
//
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void storeLobbyCommands() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(Database.getInstance().getMasterCommandList());
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("lobbyCommands.json"));
            out.print(jsonStr);
            out.close();

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

//    public void loadLobbyCommands() {
//        Gson gson = new GsonBuilder().create();
//        Type typeLobby = new TypeToken<ArrayList<Command>>(){}.getType();
//        try
//        {
//            JsonReader cmdReader = new JsonReader(new FileReader("lobbyCommands.json"));
//            ArrayList<Command> lobbyCommands = gson.fromJson(cmdReader, typeLobby);
//            Database.getInstance().setMasterCommandList(lobbyCommands);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void loadCommands() {
        Gson gson = new GsonBuilder().create();
        Type typeGame = new TypeToken<HashMap<String, ArrayList<Command>>>(){}.getType();
        Type typeLobby = new TypeToken<ArrayList<Command>>(){}.getType();
        try
        {
            JsonReader cmdReader = new JsonReader(new FileReader("lobbyCommands.json"));
            ArrayList<Command> lobbyCommands = gson.fromJson(cmdReader, typeLobby);
            Database.getInstance().setMasterCommandList(lobbyCommands);

            cmdReader = new JsonReader(new FileReader("gameCommands.json"));
            HashMap<String, ArrayList<Command>> gameCommands = gson.fromJson(cmdReader, typeGame);
            Database.getInstance().setGameCommands(gameCommands);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
