package com.sql;

import com.shared.CommandDAO;

import java.sql.SQLException;

/**
 * Created by jesjames on 4/17/18.
 */

public class main {
    public static void main(String args[]){
            CommandDAO commandDAO= new commandDAO();
        try {
            commandDAO.storeGameCommands("dummyString");
            String result = commandDAO.loadGameCommands();
            System.out.print(result);
            commandDAO.clear();
            String result2 = commandDAO.loadGameCommands();
            System.out.println(result2);
            commandDAO.storeLobbyCommands("dummyString");
            result = commandDAO.loadLobbyCommands();
            System.out.print(result);
            commandDAO.clear();
            result2 = commandDAO.loadLobbyCommands();
            System.out.print(result2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(false);}

}
