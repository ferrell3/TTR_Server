package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

import Models.Cards.DestinationCard;
import Models.Cards.DestinationDeck;
import Models.Cards.TrainCard;
import Models.Cards.TrainDeck;
import Server.Database;

public class Game {

    private String id;
    private List<Player> players; //list of players' usernames
    private List<String> playerNames;
    private ArrayList<String> chats;  //List of all chats (format of "username: msg" )
    private List<Route> routes;
    private TrainDeck trainDeck;
    private DestinationDeck destinationDeck;
    private List<TrainCard> faceUpCards;
    private boolean active = false;    //Has the game started

    private GameHistory history;

    public Game(){
        players = new ArrayList<>();
        chats = new ArrayList<>();
        trainDeck = new TrainDeck();
        destinationDeck = new DestinationDeck();
        playerNames = new ArrayList<>();
        history = new GameHistory();
        routes = Database.getInstance().getRoutes();
    }

    //constructor allowing to instantiate new game with given id
    public Game(String id){
        this.id = id;
        players = new ArrayList<>();
        chats = new ArrayList<>();
        trainDeck = new TrainDeck();
        destinationDeck = new DestinationDeck();
        playerNames = new ArrayList<>();
        history = new GameHistory();
        routes = Database.getInstance().getRoutes();
        faceUpCards = new ArrayList<>();
    }

    public void addChatMessage(String message){
        chats.add(message);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean addPlayer(Player player) {
        return players.add(player) && playerNames.add(player.getName());
    }

    public void removePlayer(String username){
        playerNames.remove(username);
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).getName().equals(username))
            {
                players.remove(i);
                break;
            }
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean isJoinable() {
        return (players.size() <5) && !active;
    }


    public ArrayList<String> getChats() {
        return chats;
    }

    public void setChats(ArrayList<String> chat) {
        this.chats = chat;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

//    public List<String> getCities() {
//        return cities;
//    }
//
//    public void setCities(List<String> cities) {
//        this.cities = cities;
//    }

    public TrainDeck getTrainDeck() {
        return trainDeck;
    }

    public void setTrainDeck(TrainDeck trainDeck) {
        this.trainDeck = trainDeck;
    }

    public DestinationDeck getDestinationDeck() {
        return destinationDeck;
    }

    public void setDestinationDeck(DestinationDeck destinationDeck) {
        this.destinationDeck = destinationDeck;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GameHistory getHistory() {
        return history;
    }

    public void setHistory(GameHistory history) {
        this.history = history;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public TrainCard drawTrainCard(){
        return trainDeck.draw();
    }

    public DestinationCard drawDestinationCard(){
        return destinationDeck.draw();
    }

    public void discardDestCards(ArrayList<DestinationCard> cards) {
        for(DestinationCard card : cards)
        {
            destinationDeck.insert(card);
        }
    }

    public List<TrainCard> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(List<TrainCard> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    public void dealFaceUp() {
        faceUpCards.add(trainDeck.draw());
    }

    public TrainCard replaceFaceUp(int index) {
        faceUpCards.get(index).setColor(trainDeck.draw().getColor());
        return faceUpCards.get(index);
    }

    public Player getPlayer(String username){
        for(Player p: players){
            if(p.getName().equals(username)){
                return p;
            }
        }
        return null;
    }
}
