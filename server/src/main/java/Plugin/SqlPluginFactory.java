package Plugin;

import com.shared.CommandDAO;
import com.shared.GameDAO;
import Interfaces.PluginFactory;
import com.shared.UserDAO;

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
