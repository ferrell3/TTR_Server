package Services;

import java.util.ArrayList;
import java.util.List;

import Interfaces.ILobby;
import Models.Command;
import Models.Game;
import Models.Request;
import Models.Result;
import Server.Database;

public class LobbyServices implements ILobby {

    private static LobbyServices theGS = new LobbyServices();

    public static LobbyServices getInstance() {
        return theGS;
    }

    private LobbyServices() {}

    //TODO: leave game when joining another?

    @Override
    public Result createGame(Request request){ //(String authToken, String gameId){
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        int commandNum = request.getCommandNum();
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            //check if gameId is empty
            if(gameId.equals(""))
            {
                result.setSuccess(false);
                result.setErrorMsg("Please enter a valid game ID.");
                System.out.println("ERROR: in createGame() -- Empty gameID");
            }
            //check if gameId already exists
            else if(!Database.getInstance().getGames().containsKey(gameId))
            {
                //if it doesn't exist yet, create it
                Game newGame = new Game(gameId);
                //leave current game
                //add the creator to the game
                String username = Database.getInstance().getUsername(authToken);
//                Database.getInstance().removePlayerFromGame(username);
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

                //TODO: fix result
                result = updateClient(request);
               // result.setSuccess(true);

                System.out.println(clientRequest.getUsername() + " created new game: "+ gameId);
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID already exists.");
                System.out.println("ERROR: in createGame() -- Duplicate gameID");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in createGame() -- Invalid auth token");
        }
        return result;
    }

    @Override
    public Result joinGame(Request request) { //(String authToken, String gameId);
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        Result result = new Result();

        //TODO: Revisit whether we still need this todo
        //TODO: revisit what should happen when attempting to re-join a game?
        //If it goes to a new screen, do that again, basically this would be how to get back to the game screen
        //if it stays in the lobby screen, print a message saying "you are already in that game"

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            //check if gameId exists
            if(Database.getInstance().getGames().containsKey(gameId))
            {
                Game currentGame = Database.getInstance().getGames().get(gameId);
                String username = Database.getInstance().getUsername(authToken);

                if(!currentGame.isJoinable())
                {
                    result.setSuccess(false);
                    result.setErrorMsg("That game is full");
                    System.out.println("ERROR: in joinGame() -- The requested game is full");
                }
                // Check if player not in any other game
                else if(Database.getInstance().findClientGame(username).equals("")) //!currentGame.getPlayers().contains(username)) {
                    {
                    // Add player to game
                    //this will need to change with the player model class (phase 2)
                    List<String> temp =  currentGame.getPlayers();
                    temp.add(username);
                    currentGame.setPlayers(temp);
                    if(currentGame.getPlayers().size() == 5)
                    {
                        currentGame.setJoinable(false);
                    }
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

                    //TODO: fix result
                    result = updateClient(request);

                    System.out.println(username+ " joined gameId: "+gameId);

                }
                else if(currentGame.getPlayers().contains(username))
                {
                    //Is this going to cause issues?
                    //TODO: Re-evaluate what happens if they are in that game
                    result.setSuccess(false);
                    result.setErrorMsg("You are already in that game.");
                    System.out.println("NOTE: in joinGame() -- Requesting user already in requested game");
                }
                else
                {
                    result.setSuccess(false);
                    result.setErrorMsg("You are already in a different game.");
                    System.out.println("ERROR: in joinGame() -- Requesting user already in a different game");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID does not exist.");
                System.out.println("ERROR: in joinGame() -- Game doesn't exist");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in joinGame() -- Invalid auth token");
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

                // Check if player is not in the current game
                if(currentGame.getPlayers().contains(username))
                {
                    // Check if game is in activeGame list
                    //if(Database.getInstance().getActiveGames().isEmpty()) //if(Database.getInstance().getActiveGames() == null)
                    if(!Database.getInstance().getActiveGames().containsKey(gameId))
                    {
                        // Add game to activeGame hashMap
                        Game activeGame = Database.getInstance().getGames().get(gameId);
                        Database.getInstance().getActiveGames().put(gameId, activeGame);

                        //create commands
                        Request clientRequest = new Request();

                        clientRequest.setGameId(gameId); //This is specific to startGame()
                        //add command for other clients
                        //creates a command object for each client except the requesting client
                        ClientProxy.getInstance().startGame(clientRequest);

                        //TODO: fix result
                        result = updateClient(request);

                        System.out.println(gameId+ " started.");
                    }
                    else
                    {
                        result.setSuccess(false);
                        result.setErrorMsg("That game is already started.");
                        System.out.println("ERROR: in startGame() -- Game is already started");
                    }
                }
                else
                {
                    result.setSuccess(false);
                    result.setErrorMsg("You cannot start that, you are not in that game.");
                    System.out.println("ERROR: in startGame() -- Requesting user is not in the requested game");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID doesn't exist.");
                System.out.println("ERROR: in startGame() -- Game doesn't exist");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in startGame() -- Invalid auth token");

        }
        return result;
    }

    @Override //polling response
    public Result updateClient(Request request) { //(String authToken);
        String authToken = request.getAuthToken();
        int commandNum = request.getCommandNum();
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

//            System.out.println("updateClient request responded to.");
        }

        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
        }
        return result;

    }


}
