package Models;

import java.util.ArrayList;

import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Gameplay.Chat;
import Models.Gameplay.Game;

public class Request {

    private String username;
    private String password;
    private String authToken;
    private String gameId;
    private int commandNum; //Gives positions on masterCommandList

    //Gameplay request features
    private Game game;      //Pass back Game object
    private String action; //Game history entry
    private ArrayList<DestinationCard> discardDest;
    private ArrayList<TrainCard> trainCards;
    private int gameCMDNum;
    private ArrayList<DestinationCard> destCards;
    private int cardIndex;


    //Chat request Features:
    private Chat chat;
    private String chatMessage;


    public Request(){
//        destCards = new ArrayList<>();
    }

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<DestinationCard> getDiscardDest() {
        return discardDest;
    }

    public void setDiscardDest(ArrayList<DestinationCard> discardDest) {
        this.discardDest = discardDest;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getGameCMDNum() {
        return gameCMDNum;
    }

    public void setGameCMDNum(int gameCMDNum) {
        this.gameCMDNum = gameCMDNum;
    }

    public ArrayList<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public ArrayList<DestinationCard> getDestCards() {
        return destCards;
    }

    public void setDestCards(ArrayList<DestinationCard> destCards) {
        this.destCards = destCards;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }
}
