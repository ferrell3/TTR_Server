package Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interfaces.IServerGame;
import Models.Command;
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


    @Override
    public Result createGame(Request request){ //(String authToken, String gameId){
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        Integer commandNum = request.getCommandNum();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
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

                //create commands
                Request clientRequest = new Request();
                //clientRequest.setAuthToken(authToken); //DO THIS FOR EACH METHOD
                clientRequest.setUsername(username); //This is specific to createGame()
                clientRequest.setGameId(gameId); //This is specific to createGame()
                //add command for other clients
                //creates a command object for each client except the requesting client
                ClientProxy.getInstance().createGame(clientRequest);

                result = updateClient(request);
               // result.setSuccess(true);

                System.out.println(clientRequest.getUsername() + " created new game: "+ gameId);
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
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            //check if gameId exists
            if(Database.getInstance().getGames().containsKey(gameId))
            {
                Game currentGame = Database.getInstance().getGames().get(gameId);
                String username = Database.getInstance().getUsername(authToken);

                // Check if player not in a current game
                if(!currentGame.getPlayers().contains(username)) {

                    // Add player to game
                    //this will need to change with the player model class (phase 2)
                    List<String> temp =  currentGame.getPlayers();
                    temp.add(username);
                    currentGame.setPlayers(temp);

                    //Add game to database with new user
                    Database.getInstance().getGames().put(gameId, currentGame);


                    //create commands
                    Request clientRequest = new Request();
                    //clientRequest.setAuthToken(authToken); //DO THIS FOR EACH METHOD
                    clientRequest.setUsername(username); //This is specific to joinGame()
                    clientRequest.setGameId(gameId); //This is specific to joinGame()
                    //add command for other clients
                    //creates a command object for each client except the requesting client
                    ClientProxy.getInstance().joinGame(clientRequest);

                    result = updateClient(request);

                    System.out.println(username+ " joined gameId: "+gameId);

                }
                else
                {
                    result.setSuccess(false);
                    result.setErrorMsg("The requested username is already part of the game.");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID doesn't exist.");
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
    public Result startGame(Request request) { //(String authToken, String gameId);
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            //check if gameId exists
            if(Database.getInstance().getGames().containsKey(gameId))
            {
                Game currentGame = Database.getInstance().getGames().get(gameId);
                String username = Database.getInstance().getUsername(authToken);

                // Check if player not in a current game
                if(currentGame.getPlayers().contains(username)) {

                    // Check if game is in activeGame list
                    if(Database.getInstance().getActiveGames() == null)
                    {
                        // Add game to activeGame hashMap
                        Game activeGame = Database.getInstance().getGames().get(gameId);
                        HashMap<String,Game> temp =  Database.getInstance().getActiveGames();
                        temp.put(gameId, activeGame);

                        //Add activeGame to database
                        Database.getInstance().setActiveGames(temp);


                        //create commands
                        Request clientRequest = new Request();

                        clientRequest.setGameId(gameId); //This is specific to startGame()
                        //add command for other clients
                        //creates a command object for each client except the requesting client
                        ClientProxy.getInstance().startGame(clientRequest);

                        result = updateClient(request);

                        System.out.println(gameId+ " started.");

                    }else if(!Database.getInstance().getActiveGames().containsKey(gameId)){

                        // Add game to activeGame hashMap
                        Game activeGame = Database.getInstance().getGames().get(gameId);
                        HashMap<String,Game> temp =  Database.getInstance().getActiveGames();
                        temp.put(gameId, activeGame);

                        //Add activeGame to database
                        Database.getInstance().setActiveGames(temp);


                        //create commands for other active clients
                        Request clientRequest = new Request();
                        //clientRequest.setAuthToken(authToken); //DO THIS FOR EACH METHOD
                        //clientRequest.setUsername(username); //This is specific to createGame()
                        clientRequest.setGameId(gameId); //This is specific to startGame()
                        //add command for other clients
                        //creates a command object for each client except the requesting client
                        ClientProxy.getInstance().startGame(clientRequest);

                        result = updateClient(request);

                        System.out.println(gameId+ " started.");


                    }else
                    {
                        result.setSuccess(false);
                        result.setErrorMsg("The requested gameId is already is already active.");
                    }

                }
                else
                {
                    result.setSuccess(false);
                    result.setErrorMsg("The requested username is already part of the game.");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID doesn't exist.");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
        }
        return result;
    }

    @Override //polling response
    public Result updateClient(Request request) { //(String authToken);
        String authToken = request.getAuthToken();
        Integer commandNum = request.getCommandNum();
       // String gameId = request.getGameId();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            ArrayList <Command> responseCommands = new ArrayList<>();
            for (int i = commandNum; i < Database.getInstance().getMasterCommandList().size(); i++){

                responseCommands.add(Database.getInstance().getMasterCommandList().get(i));

            }
            result.setSuccess(true);
            result.setUpdateCommands(responseCommands);


            System.out.println("updateClient request responded to.");
        }

        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
        }
        return result;

    }


}
