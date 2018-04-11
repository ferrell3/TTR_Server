package Plugin;

import Interfaces.CommandDAO;
import Interfaces.GameDAO;
import Interfaces.PluginFactory;
import Interfaces.UserDAO;

public class SqlPluginFactory implements PluginFactory{
    @Override
    public GameDAO createGameDAO() {
        return null;
    }

    @Override
    public CommandDAO createCommandDAO() {
        return null;
    }

    @Override
    public UserDAO createUserDAO() {
        return null;
    }
}
