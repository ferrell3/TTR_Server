package Services;

import java.util.ArrayList;

import Interfaces.ILobby;
import Models.Command;
import Models.Gameplay.Game;
import Models.Request;
import Models.Result;
import Server.Database;

public class LobbyServices implements ILobby {

    private static LobbyServices theGS = new LobbyServices();

    public static LobbyServices getInstance() {
        return theGS;
    }

    private LobbyServices() {}

    @Override
    public Result createGame(Request request){ //(String authToken, String gameId){
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
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
                String username = Database.getInstance().getUsername(authToken);

                //if it doesn't exist yet, create it
                Database.getInstance().getGames().put(gameId, new Game(gameId));

                //add the creator to the game
                //add command for other clients
                request.setUsername(username);
                ClientProxy.getInstance().createGame(request);
                result = updateClient(request);
                System.out.println(username + " created new game: "+ gameId);
                joinGame(request);
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

        //revisit what should happen when attempting to re-join a game?
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
                else if(currentGame.getPlayers().contains(username))
                {
                    result.setSuccess(false);
                    result.setErrorMsg("You are already in that game.");
                    System.out.println("NOTE: in joinGame() -- Requesting user already in requested game");
                }
                else //if(Database.getInstance().findClientGame(username).equals("")) //!currentGame.getPlayers().contains(username)) {
                {
                    // requesting client's current game
                    String prevGame = Database.getInstance().findClientGame(username);
                    if(!prevGame.equals(""))
                    {
                        //prepare request for leaveGame
                        request.setGameId(prevGame);
                        leaveGame(request);
                        //reset request to the current join request
                        request.setGameId(gameId);
                    }
                    // Add player to game
                    //this will need to change with the player model class (phase 2)
                    currentGame.getPlayers().add(username);

                    Database.getInstance().getGames().put(gameId, currentGame);
                    request.setUsername(username);
                    ClientProxy.getInstance().joinGame(request);
                    result = updateClient(request);
                    System.out.println(username+ " joined gameId: "+gameId);
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
    public Result leaveGame(Request request) {
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
                //check if user is in that game
                if(Database.getInstance().findClientGame(username).equals(gameId))
                {
                    currentGame.getPlayers().remove(username);
                    //Add game to database without that user
                    Database.getInstance().getGames().put(gameId, currentGame);
                    request.setUsername(username);
                    ClientProxy.getInstance().leaveGame(request);
                    result = updateClient(request);
                    System.out.println(username + " left game "+gameId);
                }
                else
                {
                    result.setSuccess(false);
                    result.setErrorMsg("Client is not in that game.");
                    System.out.println("ERROR: in leaveGame() -- Requesting user is not in that game");
                }
            }else
            {
                result.setSuccess(false);
                result.setErrorMsg("The requested game ID does not exist.");
                System.out.println("ERROR: in leaveGame() -- Game doesn't exist");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in leaveGame() -- Invalid auth token");
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
                    if(!currentGame.isActive())
                    {
                        // Add game to activeGame hashMap
                        Game activeGame = Database.getInstance().getGames().get(gameId);
                        activeGame.setActive(true);
                        Database.getInstance().getGames().put(gameId, activeGame);

                        ClientProxy.getInstance().startGame(request);
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
        Result result = new Result();

        //check if requesting client is an active (logged in) client
        if(Database.getInstance().getClients().contains(authToken))
        {
            ArrayList <Command> responseCommands = new ArrayList<>();
            for (int i = commandNum; i < Database.getInstance().getMasterCommandList().size(); i++)
            {
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
