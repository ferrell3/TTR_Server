package Plugin;

import DAOs.JsonCommandDAO;
import DAOs.JsonGameDAO;
import DAOs.JsonUserDAO;
import com.example.CommandDAO;
import com.example.GameDAO;
import Interfaces.PluginFactory;
import com.example.UserDAO;

public class JsonPluginFactory implements PluginFactory {
    @Override
    public GameDAO createGameDAO() {
        return new JsonGameDAO();
    }

    @Override
    public CommandDAO createCommandDAO() {
        return new JsonCommandDAO();
    }

    @Override
    public UserDAO createUserDAO() {
        return new JsonUserDAO();
    }
}
