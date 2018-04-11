package DAOs;

import java.util.ArrayList;

import Interfaces.CommandDAO;
import Models.Command;

public class SqlCommandDAO implements CommandDAO {
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
