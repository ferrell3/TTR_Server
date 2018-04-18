package com.json;

import com.shared.CommandDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class jsonCommandDAO implements CommandDAO {

    @Override
    public String loadLobbyCommands(){ //load commands from json files
        String jsonLobbyCommands = "";
        try
        {
            Scanner scanner = new Scanner(new FileReader("lobbyCommands.json"));
            jsonLobbyCommands = scanner.useDelimiter("\\A").next();
            scanner.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return jsonLobbyCommands;
    }

    @Override
    public void storeLobbyCommands(String jsonStr) { //store lobby commands in json file
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

    @Override
    public String loadGameCommands(){ //load game commands from json file
        String jsonGameCommands = "";
        try
        {
            Scanner scanner = new Scanner(new FileReader("gameCommands.json"));
            jsonGameCommands = scanner.useDelimiter("\\A").next();
            scanner.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return jsonGameCommands;
    }

    @Override
    public void storeGameCommands(String jsonStr) { //store game commands in json file
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
    public void clear(){
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("gameCommands.json"));
            out.print("{}");
            out.close();

            out = new PrintWriter(new FileWriter("lobbyCommands.json"));
            out.print("{}");
            out.close();


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void clearGameCommands() {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("gameCommands.json"));
            out.print("{}");
            out.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
