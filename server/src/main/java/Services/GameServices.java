package Services;

import java.util.ArrayList;
import java.util.List;
import Interfaces.IGame;
import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Command;
import Models.Gameplay.Player;
import Models.Request;
import Models.Result;
import Server.Database;

public class GameServices implements IGame {
    private static GameServices theOne = new GameServices();

    public static GameServices getInstance() {
        return theOne;
    }

    private GameServices() {}

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
                    request.setPlay(startGame);
                    addGameHistory(request);

                    // setPlayerColor, assignPlayerOrder, and dealCards for each player object
                    setupPlayer(gameId);
                    dealCards(gameId);
                    request.setGame(Database.getInstance().getGameById(gameId));
                    // Create cmdObject for setupGame by passing entire game object
                    GameProxy.getInstance().setupGame(request);
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
            Request request = new Request();
            request.setGameId(gameId);
            request.setPlay(gameMessage);
            addGameHistory(request);

            // Assign order
            players.get(i).setTurn(i+1);
        }
        //Replace database players list with updated players
        Database.getInstance().getGameById(gameId).setPlayers(players);
    }


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
        //TODO: how many face up cards again?
        for(int i = 0; i < 5; i++)
        {
            Database.getInstance().getGameById(gameId).dealFaceUp();
        }
    }

    public Result dealFaceUp(Request request) {
        String gameId = request.getGameId();
        Result result = new Result();
        //TODO: finish this with all the checks and the proper result object with the update commands
        //do we really need to do all the checks every time?
        //could make another method -- validRequest(Request request) that checks authToken and gameId, further checks can be done in each method
        Database.getInstance().getGameById(gameId).dealFaceUp();
        return result;
    }

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

                Database.getInstance().getGameById(gameId).discardDestCards(request.getDiscardDest());
                result.setSuccess(true);
                //add game history
                request.setPlay(play);
                addGameHistory(request);
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

    //polling response
    public Result updateClient(Request request) { //(String authToken);
        String authToken = request.getAuthToken();
        int commandNum = request.getCommandNum();
        String gameId = request.getGameId();
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

                System.out.println("updateClient successful for game: " + gameId);
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("ERROR: in updateClient() -- Invalid auth token");
        }
        return result;
    }

    @Override // Add game history to database and create cmd object
    public Result addGameHistory(Request request){
        String gameId = request.getGameId();
        String play = request.getPlay();

        // Add game history to database
        Database.getInstance().addGameHistory(gameId, play);

        // Create game history object
        GameProxy.getInstance().addGameHistory(request);
        return null;
    }
}
