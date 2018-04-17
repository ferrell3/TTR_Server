package Interfaces;

import com.example.CommandDAO;
import com.example.GameDAO;
import com.example.UserDAO;

public interface PluginFactory {
    public GameDAO createGameDAO();
    public CommandDAO createCommandDAO();
    public UserDAO createUserDAO();
}
