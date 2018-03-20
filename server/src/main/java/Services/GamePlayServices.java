package Services;

import java.util.ArrayList;
import java.util.List;
import Interfaces.IGamePlay;
import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Command;
import Models.Gameplay.Player;
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

                    // setPlayerColor, assignPlayerOrder, and dealCards for each player object
                    setupPlayer(gameId);
                    dealCards(gameId);
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
            System.out.println(e);
        }
    }


    /**
     * Updates the player's color and turn order in the game object
     *
     * @param gameId
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
        List<Player> players = Database.getInstance().getGameById(gameId).getPlayers();

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
     * @param gameId
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
        List<Player> players = Database.getInstance().getGameById(gameId).getPlayers();

        // Lookup each users in game
        for(int i = 0; i < players.size(); i++)
        {
            List<TrainCard> hand = new ArrayList<>();
            List<DestinationCard> destHand = new ArrayList<>();

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
            players.get(i).setDestination_cards(destHand);
        }

        //Replace database players list with updated players
        Database.getInstance().getGameById(gameId).setPlayers(players);

        //deal the faceUp cards to the game
        for(int i = 0; i < 5; i++)
        {
            Database.getInstance().getGameById(gameId).dealFaceUp();
        }
    }

    /**
     * @param request
     * @return result
     */
    public Result dealFaceUp(Request request) {
        String gameId = request.getGameId();
        Result result = new Result();
        //TODO: finish this with all the checks and the proper result object with the update commands
        //do we really need to do all the checks every time?
        //could make another method -- validRequest(Request request) that checks authToken and gameId, further checks can be done in each method
        Database.getInstance().getGameById(gameId).dealFaceUp();
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
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        String username = request.getUsername();
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
//                result.setSuccess(true);
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
                System.out.println("ERROR: in updateClient() -- Empty gameID.");
            }
            else
            {
                ArrayList <Command> responseCommands = new ArrayList<>();
                for (int i = commandNum; i < Database.getInstance().getGameCommands(gameId).size(); i++)
                {
                    responseCommands.add(Database.getInstance().getGameCommands(gameId).get(i));
                }
                result.setSuccess(true);
                result.setUpdateCommands(responseCommands);

                System.out.println("updateClient successful for " + username + " in game: " + gameId);
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


    //TODO test drawDestCard. Created method but haven't tested yet
    @Override
    public Result drawDestCard(Request request){
        Result result = new Result();
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();

        // Check if requesting client is an active (logged in) client
        if (Database.getInstance().getClients().contains(authToken))
        {
            // Check if game doesn't exist, return error
            if (!Database.getInstance().getGames().containsKey(gameId))
            {
                System.out.println("ERROR: in drawDestCard() -- Empty gameID.");
            }
            else
            {
                // Deal card from Game object
                ArrayList <DestinationCard> dealDest = new ArrayList<>();

                // Deal three destination cards
                for (int i = 0; i < 3; i++) {
                    dealDest.add(Database.getInstance().getGameById(gameId).drawDestinationCard());
                }
                request.setDestCard(dealDest);

                // Create cmdObject for drawDestCard
                GamePlayProxy.getInstance().drawDestCard(request);

                // Add game history
                String dealCardHistory = gameId + " drew three destination cards";
                request.setAction(dealCardHistory);
                addGameHistory(request);

                result.setSuccess(true);
                System.out.println("drawDestCard successful for game: " + gameId);
            }
        }
        else
        {
            System.out.println("ERROR: in drawDestCard() -- Invalid auth token");
        }

        return result;

    }
}
