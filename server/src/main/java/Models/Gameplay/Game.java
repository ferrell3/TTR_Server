package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

import Models.Cards.DestinationCard;
import Models.Cards.DestinationDeck;

public class Game {

    private String id;
    private List<String> players; //list of players' usernames
    private List<String> chat;  //List of all chats (format of "username: msg" )
    private List<Route> Routes;
    private List<String> Cities;
    private boolean joinable = true;
    private DestinationCard destinationCard;
    private DestinationDeck destinationDeck;
    private boolean active = false;    //Has the game started
    private GameHistory history;



    public Game(){
        players = new ArrayList<>();
    }

    //constructor allowing to instantiate new game with given id
    public Game(String id){
        this.id = id;
        players = new ArrayList<>();
        chat = new ArrayList<>();
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

    public List<String> getChat() {
        return chat;
    }

    public void setChat(List<String> chat) {
        this.chat = chat;
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
}
