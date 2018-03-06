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

/**
 * Created by kiphacking on 3/3/18.
 */

public class GameServices implements IGame {

    private static GameServices theGS = new GameServices();

    public static GameServices getInstance() {
        return theGS;
    }

    private GameServices() {}


    // Called in startGame when game starts
    public void setupGame(Request request) {
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        String username = Database.getInstance().getUsername(authToken);


        try {
            // Check if requesting client is an active (logged in) client
            if (Database.getInstance().getClients().contains(authToken)) {

                // Check if game doesn't exist, return error
                if (!Database.getInstance().getGames().containsKey(gameId)) {
                    System.out.println("ERROR: in setupGame() -- Empty gameID.");
                }

                else{
                    // Add game history
                    String startGame = gameId + " started!";
                    addGameHistory(gameId, startGame);

                    // setPlayerColor, assignPlayerOrder, and dealCards for each player object
                    setupPlayer(gameId);
                    dealCards(gameId);

                    // Create cmdObject for setupGame by passing entire game object
                    GameProxy.getInstance().setupGame(Database.getInstance().getGames().get(gameId), gameId);

                    System.out.println("setupGame successful for game: " + gameId);
                }
            } else {
                System.out.println("ERROR: in setupGame() -- Invalid auth token");
            }

        }catch(Exception e){
            System.out.println(e);
        }

    }


    // Assign order and color to each player in game
    private void setupPlayer(String gameId){
        String [] colors = {"red","green","blue","black","yellow"};

        List<Player> players = Database.getInstance().getGames().get(gameId).getPlayers();

        // Lookup each users in game
        for(int i = 0; i < players.size(); i++){
            // Assign users a color
            players.get(i).setColor(colors[i]);

            // Add game history
            String playerColor = players.get(i).getColor();
            String playerName = players.get(i).getName();
            String gameMessage = playerName + " is assigned the color " + playerColor + ".";

            addGameHistory(gameId,gameMessage);

            // Assign order
            players.get(i).setTurn(i+1);
        }

        //Replace database players list with updated players
        Database.getInstance().getGames().get(gameId).setPlayers(players);

    }


    // Deal cards (destination and train) to each player in game
    private void dealCards(String gameId){
        List<Player> players = Database.getInstance().getGames().get(gameId).getPlayers();

        // Lookup each users in game
        for(int i = 0; i < players.size(); i++){

            List<TrainCard> hand = new ArrayList<>();
            List<DestinationCard> destHand = new ArrayList<>();

            // Deal train cards
            for(int j = 0; j < 4; j++) {
                hand.add(Database.getInstance().getGames().get(gameId).drawTrainCard());
            }

            players.get(i).setHand(hand);

            // Deal destination cards
            for(int z = 0; z < 3; z++) {
                destHand.add(Database.getInstance().getGames().get(gameId).drawDestinationCard());
            }
            players.get(i).setDestination_cards(destHand);
        }

        //Replace database players list with updated players
        Database.getInstance().getGames().get(gameId).setPlayers(players);

    }

    @Override //polling response
    public Result updateClient(Request request) { //(String authToken);
        String authToken = request.getAuthToken();
        int commandNum = request.getCommandNum();
        String gameId = request.getGameId();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            // Check if game doesn't exist, return error
            if (!Database.getInstance().getGames().containsKey(gameId)) {
                System.out.println("ERROR: in updateClient() -- Empty gameID.");
            }

            else{

                ArrayList <Command> responseCommands = new ArrayList<>();
                for (int i = commandNum; i < Database.getInstance().getGameCommands().get(gameId).size(); i++)
                {
                    responseCommands.add(Database.getInstance().getGameCommands().get(gameId).get(i));
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

    // Add game history to database and create cmd object
    private void addGameHistory(String gameId, String message){
        Request request = new Request();
        request.setGameId(gameId);
        request.setPlay(message);

        // Add game history to database
        Database.getInstance().addGameHistory(gameId, message);

        // Create game history object
        GameProxy.getInstance().addGameHistory(request);

    }


}
