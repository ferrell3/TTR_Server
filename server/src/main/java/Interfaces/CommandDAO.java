package Interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import Models.Command;

/**
 * Created by ferrell3 on 4/9/18.
 */

public interface CommandDAO {
//    HashMap<String, ArrayList<Command>> getGameCommands();
//    void setGameCommands(HashMap<String, ArrayList<Command>> commands);
    ArrayList<Command> getGameCommands();
    void setGameCommands(ArrayList<Command> commands);
    void addCommand(Command cmd);
    void removeCommand(Command cmd);
}
