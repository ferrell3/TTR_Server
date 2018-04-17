package Plugin;

import com.example.CommandDAO;
import com.example.GameDAO;
import Interfaces.PluginFactory;
import com.example.UserDAO;

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
