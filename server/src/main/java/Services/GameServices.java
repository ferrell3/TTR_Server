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
    //TODO: Finish implementing IServerGame and adding functionality to methods

    //TODO: Remember to add the authToken to each request in order to skip it while adding commands

    //this one should be done
    @Override
    public Result createGame(String authToken, String gameId){
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

                Request request = new Request();
                request.setAuthToken(authToken); //DO THIS FOR EACH METHOD
                request.setUsername(username); //This is specific to createGame()
                request.setGameId(gameId); //This is specific to createGame()
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



}
