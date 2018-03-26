package Models.Gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;

public class Player {
    private String color;
    private String name;
    private boolean turn;
    private int numTrains; //will be decremented when a route is claimed
    private ArrayList<Route> claimedRoutes;
    private ArrayList<TrainCard> hand;
    private ArrayList<DestinationCard> destination_cards;
    private ArrayList<DestinationCard> drawnDestCards;
    private int longestPathSize;
    private Score score;
    private boolean lastRound;
    private int playerRank;


    public Player() {
        claimedRoutes = new ArrayList<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        drawnDestCards = new ArrayList<>();
        numTrains = 45;
        score = new Score();
    }

    public Player(String username){
        name = username;
        claimedRoutes = new ArrayList<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        drawnDestCards = new ArrayList<>();
        numTrains = 45;
        turn = false;
        score = new Score();
    }

    public Score getScore() {
        return score;
    }
    public int getTotalScore(){

        return score.getTotal();
    }

    public void setScore(Score score) {
        this.score = score;
    }

    // Map of all cities in claimed routes without duplicates
    public HashMap<String, Integer> getListClaimedRouteCities(){
        ArrayList <String> cityList = new ArrayList<>();
        HashMap<String, Integer> cities = new HashMap<>();

        for(int i = 0; i < claimedRoutes.size(); i++){
            cityList.add(claimedRoutes.get(i).getStartCity());
            cityList.add(claimedRoutes.get(i).getEndCity());
        }

        int count = 0;
        for(int i = 0; i < cityList.size(); i++){

            if(!cities.containsKey(cityList.get(i))){
                cities.put(cityList.get(i), count);
                count++;
            }
        }

        return cities;
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

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public ArrayList<Route> getClaimedRoutes() {
        return claimedRoutes;
    }

    public void setClaimedRoutes(ArrayList<Route> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }

    public List<TrainCard> getHand() {
        return hand;
    }

    // Check if user has enough train cards to claim a route
    public boolean checkHand(Route route){
        int routeLength = route.getLength();
        String routeColor = route.getColor();

        int count = 0;
        for(int i = 0; i < hand.size(); i++){

            if(hand.get(i).getColor().equals(routeColor)){
                count++;

            }else if(hand.get(i).getColor().equals("wild")){
                count++;

            }
            if(count == routeLength){
                return true;
            }
        }

        return false;
    }

    // Remove train cards from hand when claiming a route
    public void removeTrainCards(Route route){
        int routeLength = route.getLength();
        String routeColor = route.getColor();
        int count = 0;

        for(int i = 0; i < hand.size(); i++){

            if(hand.get(i).getColor().equals(routeColor)){
                hand.remove(i);
                count++;
                i--;

            }else if(hand.get(i).getColor().equals("wild")){
                hand.remove(i);
                count++;
                i--;
            }

            if(routeLength == count){
                break;
            }
        }

    }

    // Adds a claimed route to the player's array of claimed routes
    public void addClaimedRoute(Route route){
        claimedRoutes.add(route);
    }

    public void setHand(ArrayList<TrainCard> hand) {
        this.hand = hand;
    }

    public ArrayList<DestinationCard> getDestination_cards() {
        return destination_cards;
    }

    public void setDestination_cards(ArrayList<DestinationCard> destination_cards) {
        this.destination_cards = destination_cards;
    }

    public int getNumTrains() {
        return numTrains;
    }

    public void setNumTrains(int numTrains) {
        this.numTrains = numTrains;
    }

    public void decrementNumTrains(int trainNum){
        numTrains -= trainNum;
    }

    public void discardDestCards(ArrayList<DestinationCard> cards)
    {
        destination_cards.removeAll(cards);
        drawnDestCards.clear();
    }

    public ArrayList<DestinationCard> getDrawnDestCards() {
        return drawnDestCards;
    }

    public void setDrawnDestCards(ArrayList<DestinationCard> drawnDestCards) {
        this.drawnDestCards = drawnDestCards;
    }

    public void drawDestCards(ArrayList<DestinationCard> destCards) {
        destination_cards.addAll(destCards);
        drawnDestCards.addAll(destCards);
    }

    public int getLongestPathSize() {
        return longestPathSize;
    }

    public void setLongestPathSize(int longestPathSize) {
        this.longestPathSize = longestPathSize;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }
}
