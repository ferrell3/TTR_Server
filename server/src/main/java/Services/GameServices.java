package Services;

import Interfaces.IGame;
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

                /*
                   Game exists: setPlayerColor, assignPlayerOrder, dealDestCard,
                    dealTrainCard and addGameHistory ("Game started")
                */
                else{
//                    Database.getInstance().getGames().get(gameId).addChatMessage(chat.displayChat());
//                    ChatProxy.getInstance().addChat(request);


//                    result.setSuccess(true);
//                    System.out.println(username + " added chat: " + "\"" + chat.displayChat() +"\"");
//                    System.out.println(Database.getInstance().getGameCommands());
                }
            } else {
                System.out.println("ERROR: in setupGame() -- Invalid auth token");
            }

        }catch(Exception e){
            System.out.println(e);
        }

    }


    public void setGameColor(Request request){
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();





    }



}
