package Interfaces;

import com.shared.CommandDAO;
import com.shared.GameDAO;
import com.shared.UserDAO;

public interface PluginFactory {
    public GameDAO createGameDAO();
    public CommandDAO createCommandDAO();
    public UserDAO createUserDAO();
}
