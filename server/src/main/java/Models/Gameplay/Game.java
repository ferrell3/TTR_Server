package Models.Gameplay;

import java.util.ArrayList;
import java.util.HashMap;

import Models.Cards.DestinationCard;
import Models.Cards.DestinationDeck;
import Models.Cards.TrainCard;
import Models.Cards.TrainDeck;
import Server.Database;

public class Game {

    private String id;
    private ArrayList<Player> players; //list of players' usernames
    private ArrayList<String> playerNames;
    private ArrayList<String> chats;  //List of all chats (format of "username: msg" )
    private TrainDeck trainDeck;
    private DestinationDeck destinationDeck;
    private ArrayList<TrainCard> faceUpCards;
    private boolean active = false;    //Has the game started
    private HashMap<Integer, Route> routesMap; // Map of unclaimed routes
    private int lastRoundCount;
    private boolean lastRound = false;

    private GameHistory history;

    public Game(){
        players = new ArrayList<>();
        chats = new ArrayList<>();
        trainDeck = new TrainDeck();
        destinationDeck = new DestinationDeck();
        playerNames = new ArrayList<>();
        history = new GameHistory();
        routesMap = Database.getInstance().getRoutes();
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
        faceUpCards = new ArrayList<>();
        routesMap = Database.getInstance().getRoutes();
    }

    public void setDoubleRoutes(){

        routesMap = Database.getInstance().getDoubleRoutes();
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

    public ArrayList<Player> getPlayers() {
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

    public void setPlayers(ArrayList<Player> players) {
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

    public boolean containsRoute(Route route){

        if(routesMap.containsKey(route.getRouteNumber())){

            return true;
        }else {
            return false;
        }
    }

    public void removeClaimedRoute(Route route){
        routesMap.remove(route.getRouteNumber());

    }

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

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
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

    public ArrayList<TrainCard> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(ArrayList<TrainCard> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    public void dealFaceUp() {
        faceUpCards.add(trainDeck.draw());
    }

    public TrainCard getFaceUpCard(int index) {
        return faceUpCards.get(index);
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

    public HashMap<Integer, Route> getRoutesMap() {
        return routesMap;
    }

    public int getLastRoundCount() {
        return lastRoundCount;
    }

    public void decLastRoundCount() {
        lastRoundCount--;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }
    public void setLastRoundCount(int lastRoundCount) {
        this.lastRoundCount = lastRoundCount;
    }
}
