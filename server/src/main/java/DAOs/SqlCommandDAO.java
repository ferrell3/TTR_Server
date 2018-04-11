package DAOs;

import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.CommandDAO;
import Models.Command;

public class SqlCommandDAO implements CommandDAO {
    @Override
    public HashMap<String, ArrayList<Command>> getGameCommands() {
        return null;
    }

    @Override
    public void setGameCommands(HashMap<String, ArrayList<Command>> commands) {

    }

    @Override
    public void addCommand(Command cmd) {

    }

    @Override
    public void removeCommand(Command cmd) {

    }

    @Override
    public void storeGameCommands() {

    }

    @Override
    public void loadCommands() {

    }

    @Override
    public void storeLobbyCommands() {

    }
}
