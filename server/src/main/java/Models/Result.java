package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import Interfaces.ICommand;
import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Gameplay.Game;

public class Result {
    private String errorMsg;
    private String authToken; //user authToken
    private String gameId; //game ID
    private boolean success;

    //game section:
    private Game game;
    private List<TrainCard> trainCards;
    private List<DestinationCard> destinationCards;
    private ArrayList <Command> updateCommands;   //list of commands to execute

    //chats section:
    private ArrayList<String> chatMessages;

    public Result() {
        trainCards = new ArrayList<>();
        chatMessages = new ArrayList<>();
        destinationCards = new ArrayList<>();
        game = new Game();
        success = false;
    }

    public ArrayList<Command> getUpdateCommands() {
        return updateCommands;
    }

    public void setUpdateCommands(ArrayList<Command> updateCommands) {
        this.updateCommands = updateCommands;
    }

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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String id) {
        gameId = id;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<String> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<String> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(List<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public List<DestinationCard> getDestinationCards() {
        return destinationCards;
    }

    public void setDestinationCards(List<DestinationCard> destinationCards) {
        this.destinationCards = destinationCards;
    }
}
