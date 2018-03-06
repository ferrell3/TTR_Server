package Services;

import com.sun.org.apache.regexp.internal.RE;

import Interfaces.IGame;
import Models.Command;
import Models.Gameplay.Game;
import Models.Request;
import Models.Result;
import Server.Database;

/**
 * Created by kiphacking on 3/3/18.
 */

public class GameProxy implements IGame {

    private static GameProxy theGP = new GameProxy();

    public static GameProxy getInstance() {
        return theGP;
    }

    private GameProxy() {
    }

    @Override //creates a setupGame command and adds it to the gameCommands list for setupGame
    public void setupGame(Request clientRequest){
        createCommand("setupGame", clientRequest);
//        Command command = new Command("Interfaces.IGame", "setupGame",
//                new String[]{ "Models.Request" }, new Request[]{ clientRequest });
//
//        //adds the command to the gameCommands arraylist in database:
//        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
    }

    @Override
    public Result discardDestCards(Request clientRequest) {
        createCommand("discardDestCards", clientRequest);
        return null;
    }

    @Override //creates an addGameHistory command and adds it to the gameCommands list
    public Result addGameHistory(Request clientRequest){
        createCommand("addGameHistory", clientRequest);
//        Command command = new Command("Interfaces.IGame", "addGameHistory",
//                new String[]{ "Models.Request" }, new Request[]{ clientRequest });
//
//        //adds the command to the gameCommands arraylist in database:
//        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
        return null;
    }

    private void createCommand(String methodName, Request clientRequest){
        Command command = new Command("Interfaces.IGame", methodName,
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });

        //adds the command to the gameCommands arraylist in database:
        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
    }


}
