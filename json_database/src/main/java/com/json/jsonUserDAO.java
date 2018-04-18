package com.json;

import com.shared.UserDAO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by ferrell3 on 4/17/18.
 */

public class jsonUserDAO implements UserDAO {

    @Override
    public String loadUsers(){ //load users from json files
        String jsonUsers = "";
        try
        {
            Scanner scanner = new Scanner(new FileReader("users.json"));
            jsonUsers = scanner.useDelimiter("\\A").next();
            scanner.close();
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return jsonUsers;
    }

    @Override
    public void storeUsers(String jsonStr) { //store users in json files
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter("users.json"));
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
            PrintWriter out = new PrintWriter(new FileWriter("users.json"));
            out.print("{}");
            out.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
