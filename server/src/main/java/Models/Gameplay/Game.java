package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

import Models.Cards.DestinationCard;
import Models.Cards.DestinationDeck;
import Models.Command;

public class Game {

    private String id;
    private List<String> players; //list of players' usernames
    private ArrayList<String> chats;  //List of all chats (format of "username: msg" )
    private List<Route> Routes;
    private List<String> Cities;
    private boolean joinable = true;
    private DestinationCard destinationCard;
    private DestinationDeck destinationDeck;
    private boolean active = false;    //Has the game started
    //chat features/data:
    private ArrayList<Command> gameCommands;
    private GameHistory history;



    public Game(){
        players = new ArrayList<>();
    }


    public void addGameCommand(Command command){
        gameCommands.add(command);
    }
    //constructor allowing to instantiate new game with given id
    public Game(String id){
        this.id = id;
        players = new ArrayList<>();
        chats = new ArrayList<>();
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

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public boolean isJoinable() {
        return joinable;
    }


    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public ArrayList<String> getChats() {
        return chats;
    }

    public void setChats(ArrayList<String> chat) {
        this.chats = chat;
    }

    public List<Route> getRoutes() {
        return Routes;
    }

    public void setRoutes(List<Route> routes) {
        Routes = routes;
    }

    public List<String> getCities() {
        return Cities;
    }

    public void setCities(List<String> cities) {
        Cities = cities;
    }

    public DestinationCard getDestinationCard() {
        return destinationCard;
    }

    public void setDestinationCard(DestinationCard destinationCard) {
        this.destinationCard = destinationCard;
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

    public ArrayList<Command> getGameCommands() {
        return gameCommands;
    }

    public void setGameCommands(ArrayList<Command> gameCommands) {
        this.gameCommands = gameCommands;
    }
}
