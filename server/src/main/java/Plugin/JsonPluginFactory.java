package Plugin;

import com.json.jsonCommandDAO;
import com.json.jsonGameDAO;
import com.json.jsonUserDAO;
import com.shared.CommandDAO;
import com.shared.GameDAO;
import Interfaces.PluginFactory;
import com.shared.UserDAO;

public class JsonPluginFactory implements PluginFactory {
    @Override
    public GameDAO createGameDAO() {
        return new jsonGameDAO();
    }

    @Override
    public CommandDAO createCommandDAO() {
        return new jsonCommandDAO();
    }

    @Override
    public UserDAO createUserDAO() {
        return new jsonUserDAO();
    }
}
