package Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Algorithms.LongestPath;
import Interfaces.IGamePlay;
import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Command;
import Models.Gameplay.Game;
import Models.Gameplay.Player;
import Models.Gameplay.Route;
import Models.Request;
import Models.Result;
import Server.Database;

/**
 *  This class handles game play method calls from the client command object
 *
 */

public class GamePlayServices implements IGamePlay {
    private static GamePlayServices theOne = new GamePlayServices();

    public static GamePlayServices getInstance() {
        return theOne;
    }

    /**
     * Constructor for GamePlayServices()
     * A singleton is used to create only one instance of this class
     */
    private GamePlayServices() {}

    /**
     * Updates a game object with player attributes: name, color, turn order,
     *      train cards, and destination cards to setup game
     *
     * @param request Request is object from client request
     *
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     * @pre request contains valid authToken and gameId for that client
     * @pre gameId = started game
     *
     * @post masterCommandList != empty
     * @post masterCommandList.size() += 1
     * @post gameHistory has 3 to 6 entries
     *
     *
     */
    @Override // Called in startGame when game starts
    public void setupGame(Request request) {
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();

        try {
            // Check if requesting client is an active (logged in) client
            if (Database.getInstance().getClients().contains(authToken))
            {
                // Check if game doesn't exist, return error
                if (!Database.getInstance().getGames().containsKey(gameId))
                {
                    System.out.println("ERROR: in setupGame() -- Empty gameID.");
                }
                else
                {
                    // Add game history
                    String startGame = gameId + " started!";
                    Database.getInstance().getGameById(gameId).getHistory().addAction(startGame);
                    Database.getInstance().getGameById(gameId).setLastRoundCount(Database.getInstance().getGamePlayers(gameId).size());

                    // setPlayerColor, assignPlayerOrder, and dealCards for each player object
                    setupPlayer(gameId);
                    dealCards(gameId);

                    // Check if greater than 3 players. If so add double routes
                    if(Database.getInstance().getGamePlayers(gameId).size() > 3){
                        Database.getInstance().getGameById(gameId).setDoubleRoutes();
                    }

                    request.setGame(Database.getInstance().getGameById(gameId));
                    // Create cmdObject for setupGame by passing entire game object
                    GamePlayProxy.getInstance().setupGame(request);
                    System.out.println("setupGame successful for game: " + gameId);
                }
            }
            else
            {
                System.out.println("ERROR: in setupGame() -- Invalid auth token");
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Updates the player's color and turn order in the game object
     *
     * @param gameId id
     *
     * @pre Database.getGameById(gameId) != null
     * @pre players.size() >= 1 && players.size() <= 5
     * @pre gameId != null
     * @pre gameId = started game
     *
     * @post game object's players have assigned String 'color' and Boolean 'turn'
     * @post gameHistory has 2-5 additional entries
     */
    // Assign order and color to each player in game
    private void setupPlayer(String gameId){
        String [] colors = {"red","green","blue","black","yellow"};
        ArrayList<Player> players = Database.getInstance().getGameById(gameId).getPlayers();

        // Lookup each users in game
        for(int i = 0; i < players.size(); i++)
        {
            // Assign users a color
            players.get(i).setColor(colors[i]);

            // Add game history
            String playerColor = players.get(i).getColor();
            String playerName = players.get(i).getName();
            String gameMessage = playerName + " is assigned the color " + playerColor + ".";
            Database.getInstance().getGameById(gameId).getHistory().addAction(gameMessage);

            // Assign player order
            if(i ==0){
                // Assign true for starting player
                players.get(i).setTurn(true);
            }

        }
        //Replace database players list with updated players
        Database.getInstance().getGameById(gameId).setPlayers(players);
    }


    /**
     * Deal destination and train cards for each player inside game object
     *
     * @param gameId id
     *
     * @pre gameId != null
     * @pre gameId = started game
     * @pre Database.getGameById(gameId) != null
     * @pre players.size() >= 2 && players.size() <= 5
     *
     * @post game object's players have assigned Object 'trainDeck' and Object 'destinationDeck'
     * @post trainDeck.size() = 4
     * @post destinationDeck.size() = 3
     *
     *
     */
    // Deal cards (destination and train) to each player in game
    private void dealCards(String gameId){
        ArrayList<Player> players = Database.getInstance().getGameById(gameId).getPlayers();

        // Lookup each users in game
        for(int i = 0; i < players.size(); i++)
        {
            ArrayList<TrainCard> hand = new ArrayList<>();
            ArrayList<DestinationCard> destHand = new ArrayList<>();

            // Deal train cards
            for(int j = 0; j < 4; j++)
            {
                hand.add(Database.getInstance().getGameById(gameId).drawTrainCard());
            }
            players.get(i).setHand(hand);

            // Deal destination cards
            for(int z = 0; z < 3; z++)
            {
                destHand.add(Database.getInstance().getGameById(gameId).drawDestinationCard());
            }
            players.get(i).drawDestCards(destHand);
        }

        //Replace database players list with updated players
        Database.getInstance().getGameById(gameId).setPlayers(players);

        //deal the faceUp cards to the game
        Request request = new Request();
        request.setGameId(gameId);
        dealFaceUpCards(request);
    }

    @Override
    public void dealFaceUpCards(Request request) {
        String gameId = request.getGameId();
        Database.getInstance().getGameById(gameId).getFaceUpCards().clear();
        for(int i = 0; i < 5; i++)
        {
            Database.getInstance().getGameById(gameId).dealFaceUp();
        }
        locomotiveCheck(gameId);
    }

    private void locomotiveCheck(String gameId) {
        //TODO: Does not reshuffle on the second time
        //check if the server properly updates the player's hand on the server too
        int locoCount = 0;
        for(TrainCard card : Database.getInstance().getGameById(gameId).getFaceUpCards())
        {
            if(card.getColor().equals("wild"))
            {
                locoCount++;
            }
        }
        if(locoCount > 2)
        {
            Request request = new Request();
            request.setGameId(gameId);
            dealFaceUpCards(request);
            request.setTrainCards(Database.getInstance().getGameById(gameId).getFaceUpCards());
            GamePlayProxy.getInstance().dealFaceUpCards(request);
        }
    }

    /**
     * @param request object
     * @return result
     */
    @Override
    public Result takeFaceUpCard(Request request) {
        Result result;// = new Result();
        String gameId = request.getGameId();
        String authToken = request.getAuthToken();
        String username = Database.getInstance().getUsername(authToken);
        request.setUsername(username);
        //add the face up card to the player's hand
        TrainCard card = new TrainCard(Database.getInstance().getGameById(gameId).getFaceUpCard(request.getCardIndex()).getColor());
        Database.getInstance().getGameById(gameId).getPlayer(username).getHand().add(card);
        //replace the requested face up card with another from the deck
        TrainCard newCard = Database.getInstance().getGameById(gameId).replaceFaceUp(request.getCardIndex());
        locomotiveCheck(gameId);
        //add the requested card to the request list and then the replacement card too
        ArrayList<TrainCard> cards = new ArrayList<>();
        cards.add(card);
        cards.add(newCard);
        request.setTrainCards(cards);
        GamePlayProxy.getInstance().takeFaceUpCard(request);
        result = updateClient(request);

        request.setAction(username + " took a face up train card.");
        addGameHistory(request);

        System.out.println(username + " took a face up train card.");

        //do we really need to do all the checks every time?
        //could make another method -- validRequest(Request request) that checks authToken and gameId, further checks can be done in each method
        return result;
    }


    @Override
    public Result drawTrainCard(Request request) {
        String gameId = request.getGameId();
        String authToken = request.getAuthToken();
        String username = Database.getInstance().getUsername(authToken);
        request.setUsername(username);
        ArrayList<TrainCard> cards = new ArrayList<>();
        cards.add(Database.getInstance().getGameById(gameId).drawTrainCard());

        // Add train card to player's hand (train cards) in database
        Database.getInstance().getGameById(gameId).getPlayer(username).addTrainCardToHand(cards.get(0));

        request.setTrainCards(cards);
        GamePlayProxy.getInstance().drawTrainCard(request);
        Result result = updateClient(request);
        request.setAction(username + " drew a train card.");
        addGameHistory(request);
        System.out.println(username + " drew a train card.");
        //could make another method -- validRequest(Request request) that checks authToken and gameId, further checks can be done in each method
        return result;
    }

    /**
     * Add 1-2 destination cards back to the deck
     *
     * @param request Request is object from client request
     *
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     * @pre request.getUsername() != null
     * @pre request.getDiscardDest().size() = 1 || 2
     * @pre request.getDiscardDest() = typeOf( ArrayList<DestinationCard> )
     * @pre request contains valid authToken, gameId, and username for that client
     * @pre gameId = started game
     *
     * @post result.setSuccess = true
     * @post destinationDeck.size() != null
     * @post destinationDeck.size() += 1 || 2
     *
     * @return Result object with boolean 'success'
     */
    @Override
    public Result discardDestCards(Request request) {
        String gameId = request.getGameId();
        String authToken = request.getAuthToken();
        String username = Database.getInstance().getUsername(authToken);
        request.setUsername(username);
        int numCards = request.getDiscardDest().size();
        Result result = new Result();

        if(Database.getInstance().getClients().contains(authToken))
        {
            if(Database.getInstance().getGames().containsKey(gameId))
            {
                String play = username + " discarded " + numCards + " destination card";
                if(numCards == 1) { play += "."; }
                else if(numCards == 2) { play += "s."; }
                else
                {
                   result.setErrorMsg("Invalid number of cards.");
                   return result;
                }
                Database.getInstance().getGameById(gameId).getPlayer(username).discardDestCards(request.getDiscardDest());
                Database.getInstance().getGameById(gameId).discardDestCards(request.getDiscardDest());
                GamePlayProxy.getInstance().discardDestCards(request);
                result = updateClient(request);
                //add game history
                request.setAction(play);
                addGameHistory(request);

                System.out.println(play);
            }
            else
            {
                result.setErrorMsg("The requested game ID does not exist.");
                System.out.println("ERROR: in joinGame() -- Game doesn't exist");
            }
        }
        else
        {
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in joinGame() -- Invalid auth token");
        }
        return result;
    }

    /**
     * Returns a list of all new Command objects since the client last requested them
     *
     * @param request Request is object from client request
     *
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     * @pre request.getUsername() != null
     * @pre request.getGameCMDNum() != null
     * @pre request contains valid authToken, gameId, CMDNum, and username for that client
     * @pre gameId = valid started game
     *
     * @post result.setSuccess = true
     * @post returns an ArrayList of command objects; if there are new commands since the client
     *      last checked with the server
     *
     * @return Result object with an ArrayList of Command objects
     */
    //polling response
    @Override
    public Result updateClient(Request request) { //(String authToken);
        String authToken = request.getAuthToken();
        int commandNum = request.getGameCMDNum();
        String gameId = request.getGameId();
        String username = Database.getInstance().getUsername(request.getAuthToken());
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            // Check if game doesn't exist, return error
            if (!Database.getInstance().getGames().containsKey(gameId))
            {
                System.out.println("ERROR: in updateClient() -- Invalid gameID.");
            }
            else
            {
                ArrayList <Command> responseCommands = new ArrayList<>();
                if(Database.getInstance().getGameCommands(gameId) != null)
                {
                    for (int i = commandNum; i < Database.getInstance().getGameCommands(gameId).size(); i++)
                    {
                        responseCommands.add(Database.getInstance().getGameCommands(gameId).get(i));
                    }
                }
                result.setSuccess(true);
                result.setUpdateCommands(responseCommands);

//                System.out.println("updateClient successful for " + username + " in game: " + gameId);
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("ERROR: in updateClient() -- Invalid auth token");
        }
        return result;
    }

    /**
     * Adds gameHistory to the database and adds a gameHistory command object to masterCommandList
     *
     * @param request Request object with gameId and action
     * @pre request != null
     * @pre request.getGameId() != null
     * @pre request.getAction() != null
     * @pre request contains valid gameId and action
     * @pre gameId = valid started game
     *
     * @post GameHistory.size() != null
     * @post GameHistory.size() += 1
     * @post masterCommandList != empty
     * @post masterCommandList.size() += 1
     *
     * @return null
     */
    @Override // Add game history to database and create cmd object
    public Result addGameHistory(Request request){
        String gameId = request.getGameId();
        String play = request.getAction();
        // Add game history to database
        Database.getInstance().addGameHistory(gameId, play);
        // Create game history object
        GamePlayProxy.getInstance().addGameHistory(request);
        return null;
    }


    @Override
    public Result drawDestCards(Request request){
        Result result = new Result();
        String gameId = request.getGameId();
        String authToken = request.getAuthToken();
        String username = Database.getInstance().getUsername(authToken);
        request.setUsername(username);

        // Check if requesting client is an active (logged in) client
        if (Database.getInstance().getClients().contains(authToken))
        {
            // Check if game doesn't exist, return error
            if (!Database.getInstance().getGames().containsKey(gameId))
            {
                System.out.println("ERROR: in drawDestCards() -- Empty gameID.");
            }
            else
            {
                // Deal card from Game object
                ArrayList <DestinationCard> dealDest = new ArrayList<>();

                // Deal three destination cards
                int numDestCards = 3;
                if(Database.getInstance().getGameById(gameId).getDestinationDeck().size() < 3)
                {
                    numDestCards = Database.getInstance().getGameById(gameId).getDestinationDeck().size();
                }
                for (int i = 0; i < numDestCards; i++)
                {
                    dealDest.add(Database.getInstance().getGameById(gameId).drawDestinationCard());
                }
                Database.getInstance().getGameById(gameId).getPlayer(username).drawDestCards(dealDest);
                request.setDestCards(dealDest);

                // Create cmdObject for drawDestCards
                GamePlayProxy.getInstance().drawDestCards(request);

                // Add game history
                String dealCardHistory = username + " drew " + numDestCards + " destination cards";
                request.setAction(dealCardHistory);
                addGameHistory(request);

                result.setSuccess(true);
                System.out.println("drawDestCards successful for game: " + gameId);
            }
        }
        else
        {
            System.out.println("ERROR: in drawDestCards() -- Invalid auth token");
        }
        return result;
    }

    @Override
    public Result claimRoute(Request request) {
        Result result = new Result();
        String gameId = request.getGameId();
        String authToken = request.getAuthToken();
        String username = Database.getInstance().getUsername(authToken);
        Route route = request.getRoute();
        ArrayList<TrainCard> trainCards = request.getTrainCards();
        request.setUsername(username);

        // Check if requesting client is an active (logged in) client
        if (Database.getInstance().getClients().contains(authToken))
        {
            // Check if game doesn't exist, return error
            if (!Database.getInstance().getGames().containsKey(gameId))
            {
                System.out.println("ERROR: in claimRoute() -- Empty gameID.");
            }
            else
            {
                // Check if route is free (game.routes)
                if(Database.getInstance().getGameById(gameId).containsRoute(route)){

                    // Check that they have enough train cards/correct color
                    if(Database.getInstance().getGameById(gameId).getPlayer(username).checkHand(route, trainCards)){

                        // Verify that player's hand contains cards that were sent
                        if(Database.getInstance().getGameById(gameId).getPlayer(username).verifyHand(trainCards)) {

                            // Set user's name to route's owner
                            route.setOwner(username);
                            // Set route's points
                            route.addRoutePoints();

                            // Remove player's used train cards for claimed route
                            Database.getInstance().getGameById(gameId).getPlayer(username).removeTrainCards(trainCards);

                            // Update request with current hand
                            request.setTrainCards(Database.getInstance().getGameById(gameId).getPlayer(username).getHand());

                            // Add to game the player's claimed route
                            Database.getInstance().getGameById(gameId).getPlayer(username).addClaimedRoute(route);

                            // Remove the route from game's available routes
                            Database.getInstance().getGameById(gameId).removeClaimedRoute(route);

                            // increment player's score (Pass player's score.addRoutePoints the length it calculates score based off length)
                            Database.getInstance().getGameById(gameId).getPlayer(username).getScore().addRoutePoints(route.getLength());

                            // Decrement number of train cars for player
                            Database.getInstance().getGameById(gameId).getPlayer(username).decrementNumTrains(route.getLength());

                            // check for "last round" if train cars are less than 3
                            int numTrains = Database.getInstance().getGameById(gameId).getPlayer(username).getNumTrains();
                            if (numTrains < 3 && !Database.getInstance().getGameById(gameId).isLastRound()) {
                                // Initiate last round
                                Database.getInstance().getGameById(gameId).setLastRound(true);
                                // Set game object count to player size+1 (accounts for initiating player to have last round)
//                            Database.getInstance().getGameById(gameId).setLastRoundCount(Database.getInstance().getGamePlayers(gameId).size());
                            }

                            // Add game history
                            request.setAction(route.getStartCity() + " to " + route.getEndCity() + " claimed by " + username);
                            addGameHistory(request);

                            // Add cmdObject that contains route object for client
                            GamePlayProxy.getInstance().claimRoute(request);


                            result.setSuccess(true);
                            System.out.println("claimRoute successful: " + route.getStartCity() + " to " + route.getEndCity() + " claimed by " + username);
                        }
                        else{
                            System.out.println("ERROR: in claimRoute() -- train cards sent don't match player's current hand");
                            result.setErrorMsg("ERROR: in claimRoute() -- train cards sent don't match player's current hand");
                        }

                    }else{
                        System.out.println("ERROR: in claimRoute() -- not enough train cards or incorrect color");
                        result.setErrorMsg("ERROR: in claimRoute() -- not enough train cards or incorrect color");
                    }

                }else {

                    System.out.println("ERROR: in claimRoute() -- route already claimed");
                    result.setErrorMsg("ERROR: in claimRoute() -- route already claimed");
                }

            }
        }
        else
        {
            System.out.println("ERROR: in claimRoute() -- Invalid auth token");
            result.setErrorMsg("ERROR: in claimRoute() -- Invalid auth token");
        }
        return result;

    }

    @Override
    public void endTurn(Request request) {
        String gameId = request.getGameId();
        String activeUser = "";
        for(int i = 1; i < Database.getInstance().getGamePlayers(gameId).size(); i++)
        {

            if (Database.getInstance().getGamePlayers(gameId).get(i-1).isTurn())
            {
                // Check if game object's lastRound is set to true
                if(Database.getInstance().getGameById(gameId).isLastRound()){

                    // Initiate game over
                    if(Database.getInstance().getGameById(gameId).getLastRoundCount() == 0){
                        endGame(request);
                    }
                    else{
                        Database.getInstance().getGameById(gameId).decLastRoundCount();
                    }
                }
                //increment game.lastRoundCount
                //set a boolean to prompt an action after the for loop is done executing:


                Database.getInstance().getGamePlayers(gameId).get(i-1).setTurn(false);
                Database.getInstance().getGamePlayers(gameId).get(i).setTurn(true);
                activeUser = Database.getInstance().getGamePlayers(gameId).get(i).getName();
                break;
            }
            else if(i == (Database.getInstance().getGamePlayers(gameId).size()-1))
            {
                if(Database.getInstance().getGamePlayers(gameId).get(i).isTurn())
                {
                    if(Database.getInstance().getGameById(gameId).isLastRound()){

                        // Initiate game over
                        if(Database.getInstance().getGameById(gameId).getLastRoundCount() == 0){
                            endGame(request);
                        }
                        else{
                            Database.getInstance().getGameById(gameId).decLastRoundCount();
                        }
                    }
                    Database.getInstance().getGamePlayers(gameId).get(i).setTurn(false);
                    Database.getInstance().getGamePlayers(gameId).get(0).setTurn(true);
                    activeUser = Database.getInstance().getGamePlayers(gameId).get(0).getName();
                }
            }

        }


        request.setUsername(activeUser);
        GamePlayProxy.getInstance().endTurn(request);
        request.setAction("It's " + activeUser + "\'s turn.");
        addGameHistory(request);
    }

    //This function packages up the game information and sends it to the Clients to execute:
    public Result endGame(Request argRequest){
//        Request request = new Request();
        Result result = new Result();
        String gameId = argRequest.getGameId();


        // calculate the longest route winner
        calculateLongestRoute(gameId);

        // calculate the destination card points
        calculateDestCard(gameId);

        // Determine winner and rank players by total score
        ArrayList<Player> playerList = Database.getInstance().getGameById(gameId).getPlayers();
        // Sort arrayList of players by total score
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.getTotalScore() < 0)
                {
                    return p1.getTotalScore() - p2.getTotalScore();
                }
                return p2.getTotalScore() - p1.getTotalScore();
            }
        });
        // set playerRank using sorted playerList
        for(int i = 0; i < playerList.size(); i++){
            Database.getInstance().getGameById(gameId).getPlayer(playerList.get(i).getName()).setPlayerRank(i+1);
        }

        // set the correct game object to send back to client
        argRequest.setGame(Database.getInstance().getGameById(gameId));
//        request.setGameId(gameId);
        // set command object to send back to client
        GamePlayProxy.getInstance().endGame(argRequest);


        Database.getInstance().getGames().remove(gameId);
        Request req = new Request();
        req.setGameId(gameId);
        ClientProxy.getInstance().removeGame(req);

        return result;
    }

    // Calculates longest route winner for a game
    public void calculateLongestRoute(String gameId){
        Game game = Database.getInstance().getGameById(gameId);

        // Calculate longest route by calling longest route algorithm
        for(int i = 0; i < game.getPlayers().size(); i++){

            int longestPathSize = LongestPath.calcLongestRoute(game.getPlayers().get(i));
            game.getPlayers().get(i).setLongestPathSize(longestPathSize);
        }

        // Determine which player has the longest route
        ArrayList<Player> playersList = game.getPlayers();
        Set<Player> highestScoringPlayers = new HashSet<>();
        int maxScore = Integer.MIN_VALUE;
        for (Player player : playersList) {
            maxScore = Math.max(maxScore, player.getLongestPathSize());
        }
        for (Player player : playersList) {
            if (player.getLongestPathSize() == maxScore) {
                highestScoringPlayers.add(player);
            }
        }

        List <Player> longestRoutePlayers = new ArrayList<>(highestScoringPlayers);

        // Check for tie for longest route and add longest route points (10pts)
        if(longestRoutePlayers.size() > 1){
            for (Player player: longestRoutePlayers) {
                game.getPlayer(player.getName()).getScore().addLongestRoad();
                System.out.println("LongestRoute by: "+player.getName()+" with a length of "+ maxScore+" train cars");
            }

        }else{
            game.getPlayer(longestRoutePlayers.get(0).getName()).getScore().addLongestRoad();
            System.out.println("LongestRoute by: "+longestRoutePlayers.get(0).getName()+" with a length of "+maxScore +" train cars");
        }

    }


    // Calculates destination card scores for each player in a game
    public void calculateDestCard(String gameId){
        Game game = Database.getInstance().getGameById(gameId);

        for(int i = 0; i < game.getPlayers().size(); i++){

            for(int j = 0; j < game.getPlayers().get(i).getDestination_cards().size(); j++){
                Player player = game.getPlayers().get(i);
                DestinationCard destCard = game.getPlayers().get(i).getDestination_cards().get(j);
                boolean destCardCompleted = LongestPath.calcDestCard(player,destCard);

                int destCardPoints = destCard.getPoints();
                if(destCardCompleted){

                    game.getPlayer(player.getName()).getScore().addPosDestPoints(destCardPoints);
                    System.out.println("Dest card completed: "+destCard.getCity1()+" to "+destCard.getCity2());
                    System.out.println("Current score: "+game.getPlayer(player.getName()).getScore().getTotal());
                }
                else{
                    game.getPlayer(player.getName()).getScore().addNegDestPoints(destCardPoints);
                    System.out.println("Dest card incomplete: "+destCard.getCity1()+" to "+destCard.getCity2());
                    System.out.println("Current score: "+game.getPlayer(player.getName()).getScore().getTotal());
                }

            }

        }

    }


//    public static void main (String[] args)
//    {
//        Route route1 = new Route("Seattle", "SF",5);
//        route1.setColor("red");
//
//        ArrayList <TrainCard> trainCards = new ArrayList<>();
//        TrainCard card1 = new TrainCard("wild");
//        TrainCard card2 = new TrainCard("wild");
//        TrainCard card3 = new TrainCard("red");
//        TrainCard card4 = new TrainCard("red");
//        TrainCard card5 = new TrainCard("red");
//        TrainCard card6 = new TrainCard("blue");
//
//        ArrayList <TrainCard> playersHand = new ArrayList<>();
//
//
//        trainCards.add(card1);  // Wild
//        trainCards.add(card4);  // red
//        trainCards.add(card4);  // red
//        trainCards.add(card1);  // Wild
//        trainCards.add(card4);  // red
////        trainCards.add(card1);
////        trainCards.add(card2);
////        trainCards.add(card3);
//        playersHand.add(card6);
//        playersHand.add(card1);
//        playersHand.add(card1);
//        playersHand.add(card1);
//        playersHand.add(card3);
//        playersHand.add(card3);
//        playersHand.add(card3);
//        playersHand.add(card3);
//        playersHand.add(card3);
//        playersHand.add(card3);
//        playersHand.add(card6);
//        playersHand.add(card6);
//
//
//        Player player = new Player();
//        player.setName("Kip");
//        player.setHand(playersHand);
//
//        player.removeTrainCards(trainCards);
//
//        for(int i = 0; i < player.getHand().size(); i++){
//            System.out.println(player.getHand().get(i).getColor());
//        }
//
//
//        // Testing of calculateDestCard and calculateLongestRoute
////        Game game = new Game();
////
////        Player player = new Player();
////        player.setName("Kip");
////        Route route1 = new Route("Seattle", "SF",4);
////        Route route2 = new Route("Seattle", "Portland",2);
////        Route route3 = new Route("Seattle", "SLC",3);
////        Route route4 = new Route("Portland", "SF",5);
////        Route route5 = new Route("Portland", "DC",6);
////        Route route6 = new Route("SF", "Langley",2);
////        Route route7 = new Route("SLC", "Langley",2);
////        Route route8 = new Route("Langley", "DC",6);
////
////        Route route9 = new Route("Dig", "pirate",6);
////
////        Player player2 = new Player();
////        player2.setName("Jordan");
////
////        Player player3 = new Player();
////        player3.setName("Finn");
////
////        ArrayList<Route> routes = new ArrayList<>(Arrays.asList(route1,route2,route3,route4,route5,route6,route7,route8,route9));
////        player.setClaimedRouteList(routes);
////        player3.setClaimedRouteList(routes);
////
////        ArrayList<Route> routes2 = new ArrayList<>(Arrays.asList(route1,route5));
////        player2.setClaimedRouteList(routes2);
////
////        DestinationCard destCard = new DestinationCard();
////        destCard.setCity1("Seattle");
////        destCard.setCity2("SF");
////        destCard.setPoints(7);
////
////        DestinationCard destCard2 = new DestinationCard();
////        destCard2.setCity1("Seattle");
////        destCard2.setCity2("Dig");
////        destCard2.setPoints(5);
////
////        ArrayList<DestinationCard> destinationCards = new ArrayList<>();
////        destinationCards.add(destCard);
////        destinationCards.add(destCard);
////
////        ArrayList<DestinationCard> destinationCards2 = new ArrayList<>();
////        destinationCards2.add(destCard2);
////        destinationCards2.add(destCard);
////
////
////        player.setDestination_cards(destinationCards);
////        player2.setDestination_cards(destinationCards);
////        player3.setDestination_cards(destinationCards2);
////
////        game.addPlayer(player);
////        game.addPlayer(player2);
////        game.addPlayer(player3);
////
////
////
////        GamePlayServices.getInstance().calculateDestCard(game);
////        boolean temp = LongestPath.calcDestCard(player, destCard);
////
////        System.out.println(temp);
////        GamePlayServices.getInstance().calculateLongestRoute(game);
//
//
//
//    }
}
