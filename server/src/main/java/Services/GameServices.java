package Services;

import java.util.ArrayList;
import java.util.List;

import Interfaces.IGame;
import Models.Cards.DestinationCard;
import Models.Cards.TrainCard;
import Models.Gameplay.Player;
import Models.Request;
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

                    // setPlayerColor, assignPlayerOrder, and dealCards for each player object
                    setupPlayer(gameId);
                    dealCards(gameId);

                    // Create cmdObject for setupGame by passing entire game object
                    GameProxy.getInstance().setupGame(Database.getInstance().getGames().get(gameId), gameId);

                    System.out.println("setupGame() successfully completed for game: " + gameId);

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



}
