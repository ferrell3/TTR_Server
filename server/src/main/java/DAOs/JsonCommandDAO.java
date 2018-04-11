package DAOs;

import java.util.ArrayList;

import Interfaces.CommandDAO;
import Models.Command;

/**
 * Created by ferrell3 on 4/9/18.
 */

public class JsonCommandDAO implements CommandDAO {
//    HashMap<String, ArrayList<Command>> getGameCommands();
//    void setGameCommands(HashMap<String, ArrayList<Command>> commands);
    @Override
    public ArrayList<Command> getGameCommands() {
        return null;
    }

    @Override
    public void setGameCommands(ArrayList<Command> commands) {

    }

    @Override
    public void addCommand(Command cmd) {

    }

    @Override
    public void removeCommand(Command cmd) {

    }
}
