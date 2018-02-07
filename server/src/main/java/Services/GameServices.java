package Services;

import java.util.ArrayList;

import Interfaces.ICommand;
import Models.Command;
import Interfaces.IServerGame;
import Models.Game;
import Models.Request;
import Models.Result;
import Server.Database;

public class GameServices implements IServerGame {

    private static GameServices theGS = new GameServices();

    public static GameServices getInstance() {
        return theGS;
    }

    private GameServices() {}

    //TODO: Finish implementing IServerGame and adding functionality to methods

    //TODO: Remember to add the authToken to each request in order to skip it while adding commands

    //this one should be done
    @Override
    public Result createGame(Request request){ //(String authToken, String gameId){
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        Result result = new Result();

        if(Database.getInstance().getClients().contains(authToken))
        {
            //check if gameId already exists
            if(!Database.getInstance().getGames().containsKey(gameId))
            {
                //if it doesn't exist yet, create it
                Game newGame = new Game(gameId);
                //add the creator to the game
                String username = Database.getInstance().getUsername(authToken);
                newGame.getPlayers().add(username);
                //add the game to the database
                Database.getInstance().getGames().put(gameId, newGame);
//                result.setGameId(gameId); //do we want to return anything other than a boolean?
                result.setSuccess(true);

                Request clientRequest = new Request();
                clientRequest.setAuthToken(authToken); //DO THIS FOR EACH METHOD
                clientRequest.setUsername(username); //This is specific to createGame()
                clientRequest.setGameId(gameId); //This is specific to createGame()
                //add command for other clients
                //creates a command object for each client except the requesting client
                ClientProxy.getInstance().createGame(request);
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID already exists.");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
        }
        return result;
    }

    @Override
    public Result joinGame(Request request) { //(String authToken, String gameId);
        return null;
    }

    @Override
    public Result startGame(Request request) { //(String authToken, String gameId);
        return null;
    }

    @Override
    public Result updateClient(Request request) { //(String authToken);
        return null;
    }


}
