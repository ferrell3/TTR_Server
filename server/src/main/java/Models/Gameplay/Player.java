package Models.Gameplay;

import java.util.ArrayList;
import java.util.List;

import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;

public class Player {
    private String color;
    private String name;
    private int points;
    private int turn;
    private int numTrains; //will be decremented when a route is claimed
    private List<Route> claimedRoutes;
    private List<TrainCard> hand;
    private List<DestinationCard> destination_cards;

    public Player() {
        claimedRoutes = new ArrayList<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        numTrains = 45;
    }

    public Player(String username){
        name = username;
        claimedRoutes = new ArrayList<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        //TODO: how many trains does a player get?
        //board game rules say 45
        numTrains = 45;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<Route> getClaimedRoutes() {
        return claimedRoutes;
    }

    public void setClaimedRoutes(List<Route> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }

    public List<TrainCard> getHand() {
        return hand;
    }

    public void setHand(List<TrainCard> hand) {
        this.hand = hand;
    }

    public List<DestinationCard> getDestination_cards() {
        return destination_cards;
    }

    public void setDestination_cards(List<DestinationCard> destination_cards) {
        this.destination_cards = destination_cards;
    }

    public int getNumTrains() {
        return numTrains;
    }

    public void setNumTrains(int numTrains) {
        this.numTrains = numTrains;
    }

    public void discardDestCards(ArrayList<DestinationCard> cards)
    {
        destination_cards.removeAll(cards);
    }
}
