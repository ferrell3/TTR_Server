package Models.Gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;

public class Player {
    private String color;
    private String name;
    private boolean turn;
    private int numTrains; //will be decremented when a route is claimedRouteList
    private ArrayList<Route> claimedRouteList;
    private Map<Integer, Route> claimedRoutes;
    private ArrayList<TrainCard> hand;
    private ArrayList<DestinationCard> destination_cards;
    private ArrayList<DestinationCard> drawnDestCards;
    private int longestPathSize;
    private Score score;
    private boolean lastRound;
    private int playerRank;


    public Player() {
        claimedRouteList = new ArrayList<>();
        claimedRoutes = new HashMap<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        drawnDestCards = new ArrayList<>();
        numTrains = 25;
        score = new Score();
    }

    public Player(String username){
        name = username;
        claimedRouteList = new ArrayList<>();
        claimedRoutes = new HashMap<>();
        hand = new ArrayList<>();
        destination_cards = new ArrayList<>();
        drawnDestCards = new ArrayList<>();
        numTrains = 25;
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

    // Map of all cities in claimedRouteList routes without duplicates
    public HashMap<String, Integer> getListClaimedRouteCities(){
        ArrayList <String> cityList = new ArrayList<>();
        HashMap<String, Integer> cities = new HashMap<>();

        for(int i = 0; i < claimedRouteList.size(); i++){
            cityList.add(claimedRouteList.get(i).getStartCity());
            cityList.add(claimedRouteList.get(i).getEndCity());
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

    public ArrayList<Route> getClaimedRouteList() {
        return claimedRouteList;
    }

    public void setClaimedRouteList(ArrayList<Route> claimedRouteList) {
        this.claimedRouteList = claimedRouteList;
    }

    public void addTrainCardToHand(TrainCard trainCard){
        this.hand.add(trainCard);

    }
    public ArrayList<TrainCard> getHand() {
        return hand;
    }

    // Verify that the cards sent match the player's hand
    public boolean verifyHand(ArrayList<TrainCard> trainCards){
        ArrayList <TrainCard> handCards = new ArrayList<>();
        handCards.addAll(hand);
        int count = 0;

        // Check each card sent to server
        for(int i = 0; i < trainCards.size(); i++){

            for(int j = 0; j < handCards.size(); j++){

                // If cards match remove from temp hand (handCards)
                if(trainCards.get(i).getColor().equals(handCards.get(j).getColor())){
                    count++;
                    handCards.remove(j);
                    break;
                }

            }
        }

        // If count matches the train cards then true, else false
        return count == trainCards.size();

    }

    // Check if user has enough/correct train cards to claim a route
    public boolean checkHand(Route route, ArrayList<TrainCard> trainCards){
        int routeLength = route.getLength();
        String routeColor = route.getColor();
        int numCards = trainCards.size();

        // Check that player has sent right number of cards
        if(routeLength != numCards){
            return false;
        }

        // Check if the route is wild or not (else)
        if(routeColor.equals("wild")){
            String wildRouteColor = "default";

            // Find other colors than wild
            for(int i = 0; i < trainCards.size(); i++){

                // Check if color is NOT wild
                if(!trainCards.get(i).getColor().equals("wild")){
                    wildRouteColor = trainCards.get(i).getColor();
                    break;
                }
            }

            // Must contain only wild cards
            if(wildRouteColor.equals("default")){
                return true;
            }

            int count = 0;
            for(int i = 0; i < trainCards.size(); i++){

                // if the train card equals same wildRouteColor OR wild
                if(trainCards.get(i).getColor().equals(wildRouteColor) || trainCards.get(i).getColor().equals("wild")){
                    count++;
                }
            }

            if(count == routeLength){
                return true;
            }


        }else{
            int count = 0;
            for(int i = 0; i < trainCards.size(); i++){

                // if the train card equals same color OR wild
                if(trainCards.get(i).getColor().equals(routeColor) || trainCards.get(i).getColor().equals("wild")){
                    count++;
                }
            }

            if(count == routeLength){
                return true;
            }
        }

        return false;
    }

    // Remove train cards from hand when claiming a route
    public void removeTrainCards(ArrayList<TrainCard> trainCards){

        for(int i = 0; i < trainCards.size(); i++){

            for(int j = 0; j < hand.size(); j++){
                // If train card matches hand remove from deck
                if(trainCards.get(i).getColor().equals(hand.get(j).getColor())){
                    hand.remove(j);
                    break;
                }
            }
        }

    }

    // Adds a claimedRouteList route to the player's array of claimedRouteList routes
    public void addClaimedRoute(Route route){
        claimedRouteList.add(route);
        claimedRoutes.put(route.getRouteNumber(), route);
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
        for(DestinationCard discard : cards)
        {
            for(DestinationCard card : destination_cards)
            {
                if(discard.isEqual(card))
                {
                    destination_cards.remove(card);
                    break;
                }
            }
        }
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
