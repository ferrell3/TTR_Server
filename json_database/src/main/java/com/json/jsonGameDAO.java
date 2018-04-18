package com.json;

import com.shared.GameDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by ferrell3 on 4/17/18.
 */

public class jsonGameDAO implements GameDAO {

    @Override
    public void storeGames(String jsonStr) { //save games in json files
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print(jsonStr);
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String loadGames(){ //load users from json files
        String jsonGames = "";
        try
        {
            Scanner scanner = new Scanner(new FileReader("games.json"));
            jsonGames = scanner.useDelimiter("\\A").next();
            scanner.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return jsonGames;
    }

    @Override
    public void clear(){
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("games.json"));
            out.print("{}");
            out.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
