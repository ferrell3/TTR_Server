package com.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ferrell3 on 4/9/18.
 */

public interface CommandDAO {
//    HashMap<String, ArrayList<Command>> getGameCommands();
//    void setGameCommands(HashMap<String, ArrayList<Command>> commands);
//    ArrayList<Command> getGameCommands();
//    void setGameCommands(ArrayList<Command> commands);
//    void addCommand(Command cmd);
//    void removeCommand(Command cmd);

//    void storeGameCommands();
//    void storeLobbyCommands();
//    void loadCommands();

    void storeLobbyCommands(String jsonStr) throws SQLException;
    void storeGameCommands(String jsonStr) throws SQLException;
    String loadLobbyCommands() throws SQLException;
    String loadGameCommands() throws SQLException;
    void clear() throws SQLException;
}
