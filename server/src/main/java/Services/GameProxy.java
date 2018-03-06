package Services;

import Interfaces.IGameProxy;
import Models.Command;
import Models.Gameplay.Game;
import Models.Request;
import Server.Database;

/**
 * Created by kiphacking on 3/3/18.
 */

public class GameProxy implements IGameProxy {

    private static GameProxy theGP = new GameProxy();

    public static GameProxy getInstance() {
        return theGP;
    }

    private GameProxy() {
    }

     //creates a setupGame command and adds it to the gameCommands list for setupGame
    public void setupGame(Game game, String gameId){
        Request request = new Request();
        request.setGame(game);
        request.setGameId(gameId);

        Command command = new Command("Interfaces.IGame", "setupGame",
                new String[]{ "Models.Request" }, new Request[]{request});

        //adds the command to the gameCommands arraylist in database:
        Database.getInstance().addGameCommand(request.getGameId(), command);
    }

    //creates an addGameHistory command and adds it to the gameCommands list
    public void addGameHistory(Request clientRequest){

        Command command = new Command("Interfaces.IGame", "addGameHistory",
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });

        //adds the command to the gameCommands arraylist in database:
        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
    }


}
