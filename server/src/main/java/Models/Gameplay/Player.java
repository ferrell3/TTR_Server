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
    private List<Route> claimedRoutes;
    private List<TrainCard> hand;
    private List<DestinationCard> destination_cards;

    public Player() {
        claimedRoutes = new ArrayList<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
    }

    public void chooseDestCard(){
        //TODO: decide how to implement this method
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
}
