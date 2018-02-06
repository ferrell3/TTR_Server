package Models;

import java.util.List;

import Interfaces.ICommand;

public class Result {
    //We don't necessarily need these variables, these are just some possible ideas
    private String errorMsg;
    private String authToken; //user authToken
    private String gameId; //game ID
    private boolean success;

    //probably won't use for a response to a client response like startGame()
    //I'm thinking more for use with the poller and updating the other clients
    private List<ICommand> commands; //list of client commands?

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getgameId() {
        return gameId;
    }

    public void setgameId(String id) {
        gameId = id;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public void setCommands(List<ICommand> commands) {
        this.commands = commands;
    }
}
