package Services;

import java.util.ArrayList;
import Interfaces.ILobby;
import Models.Command;
import Models.Gameplay.Game;
import Models.Gameplay.Player;
import Models.Request;
import Models.Result;
import Server.Database;

/**
 * LobbyServices implements the functionality in the game lobby.
 * These methods are executed by the command object when the client sends a request
 * to the server.
 */
public class LobbyServices implements ILobby {

    private static LobbyServices theOne = new LobbyServices();

    /**
     * Returns the only instance of this class
     */
    public static LobbyServices getInstance() { return theOne; }

    private LobbyServices() {}

    /**
     * Creates a new game and adds the client as a player and returns a result object containing a
     * list of commands for the client
     *
     * @param request A request object to transfer the necessary data to the server
     *                to fulfill this request to create and join a new game.
     *
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     *
     * @post result.getUpdateCommands() != null
     * @post result.getUpdateCommands().size >= 2 and contains at least the createGame and joinGame commands
     */
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

                //add commands
                request.setUsername(username);
                ClientProxy.getInstance().createGame(request);
                System.out.println(username + " created new game: "+ gameId);
                result = joinGame(request);
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

    /**
     * Removes the client from any other game and adds them to the requested game and returns a
     * result object containing a list of commands for the client
     *
     * @param request A request object to transfer the necessary data to the server
     *                to fulfill this request to join the specified game.
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     *
     * @post result.getUpdateCommands() != null
     * @post result.getUpdateCommands().size >= 1 and contains at least the joinGame command
     */
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
                Game currentGame = Database.getInstance().getGameById(gameId);
                String username = Database.getInstance().getUsername(authToken);
                Player player = new Player(username);

                if(!currentGame.isJoinable())
                {
                    result.setSuccess(false);
                    result.setErrorMsg("That game is full");
                    System.out.println("ERROR: in joinGame() -- The requested game is full");
                }
                else if(currentGame.getPlayerNames().contains(username))
                {
                    result.setSuccess(false);
                    result.setErrorMsg("You are already in that game.");
                    System.out.println("NOTE: in joinGame() -- Requesting user already in requested game");
                }
                else
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
                    currentGame.addPlayer(player);

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

    /**
     * Removes the player from their current game and returns a result object containing a list of
     * commands for the client
     *
     * @param request A request object to transfer the necessary data to the server
     *                to fulfill this request to leave the specified game.
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     *
     * @post result.getUpdateCommands() != null
     * @post result.getUpdateCommands().size >= 1 and contains at least the leaveGame command
     */
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
                Game currentGame = Database.getInstance().getGameById(gameId);
                String username = Database.getInstance().getUsername(authToken);
                //check if user is in that game
                if(Database.getInstance().findClientGame(username).equals(gameId))
                {
                    currentGame.removePlayer(username);
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

    /**
     * Starts a game, calls setupGame() in the GamePlayServices and returns a result object
     * containing a list of commands for the client
     *
     * @param request A request object to transfer the necessary data to the server
     *                to fulfill this request to start the specified game.
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getGameId() != null
     *
     * @post result.getUpdateCommands() != null
     * @post result.getUpdateCommands().size >= 1 and contains at least the startGame and setupGame commands
     */
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
                Game currentGame = Database.getInstance().getGameById(gameId);
                String username = Database.getInstance().getUsername(authToken);

                // Check if player is not in the current game
                if(currentGame.getPlayerNames().contains(username))
                {
                    // Check if game is in activeGame list
                    if(!currentGame.isActive())
                    {
                        // Add game to activeGame hashMap
                        Game activeGame = Database.getInstance().getGameById(gameId);
                        activeGame.setActive(true);
                        Database.getInstance().getGames().put(gameId, activeGame);

                        ClientProxy.getInstance().startGame(request);

                        // setupGame and create cmdObjects (player order, color, and cards)
                        GamePlayServices.getInstance().setupGame(request);

                        result = updateClient(request);
                        System.out.println(gameId + " started.");
                    }
                    else
                    {
                        result.setErrorMsg("That game is already started.");
                        System.out.println("ERROR: in startGame() -- Game is already started");
                    }
                }
                else
                {
                    result.setErrorMsg("You cannot start that, you are not in that game.");
                    System.out.println("ERROR: in startGame() -- Requesting user is not in the requested game");
                }
            }
            else
            {
                result.setErrorMsg("The requested game ID doesn't exist.");
                System.out.println("ERROR: in startGame() -- Game doesn't exist");
            }
        }
        else
        {
            result.setErrorMsg("Invalid authorization token.");
            System.out.println("ERROR: in startGame() -- Invalid auth token");
        }
        return result;
    }

    /**
     * Responds to the client's poller and returns a result object containing a list of commands
     * that the client has not yet received
     *
     * @param request A request object to transfer the necessary data to the server
     *                to fulfill this request.
     *
     * @pre request != null
     * @pre request.getAuthToken() != null
     * @pre request.getCommandNum() != null
     * @pre request.getCommandNum() < Database.getMasterCommandList().size();
     *
     * @post result != null
     * @post result.getUpdateCommands() != null
     * @post result.getUpdateCommands().size() >= 0
     */
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
            //System.out.println("updateClient request responded to.");
        }
        else
        {
            result.setErrorMsg("ERROR: in updateClientLobby() -- Invalid auth token");
        }
        return result;
    }
}
