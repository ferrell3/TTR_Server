package Interfaces;

public interface PluginFactory {
    public GameDAO createGameDAO();
    public CommandDAO createCommandDAO();
    public UserDAO createUserDAO();
}
