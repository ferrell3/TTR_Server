package Models;

import Models.Gameplay.Chat;
import Models.Gameplay.Game;

public class Request {

    //test

    private String username;
    private String password;
    private String authToken;
    private String gameId;
    private boolean status; //Game started or not?, etc.
    private int commandNum; //Gives positions on masterCommandList
    //Chat request Features:
    private Chat chat;
    private int chatNum;
    private String chatMessage;
    private Game game;      //Pass back Game object


    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }


    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getCommandNum() {
        return commandNum;
    }

    public void setCommandNum(int commandNum) {
        this.commandNum = commandNum;
    }

    public Request(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getChatNum() {
        return chatNum;
    }

    public void setChatNum(int chatNum) {
        this.chatNum = chatNum;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
