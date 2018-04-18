package Plugin;

import com.shared.CommandDAO;
import com.shared.GameDAO;
import Interfaces.PluginFactory;
import com.shared.UserDAO;
import com.sql.sqlGameDAO;
import com.sql.sqlCommandDAO;
import com.sql.sqlUserDAO;


public class SqlPluginFactory implements PluginFactory{
    @Override
    public GameDAO createGameDAO() {
        return new sqlGameDAO();
    }

    @Override
    public CommandDAO createCommandDAO() {
        return new sqlCommandDAO();
    }

    @Override
    public UserDAO createUserDAO() {
        return new sqlUserDAO();
    }
}
