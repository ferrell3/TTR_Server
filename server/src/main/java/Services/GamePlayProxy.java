package Services;

import java.util.ArrayList;

import Interfaces.IGamePlay;
import Models.Command;
import Models.Request;
import Models.Result;
import Server.Database;


class GamePlayProxy implements IGamePlay {

    private static GamePlayProxy theGP = new GamePlayProxy();

    public static GamePlayProxy getInstance() {
        return theGP;
    }

    private GamePlayProxy() {
    }

    @Override //creates a setupGame command and adds it to the gameCommands list for setupGame
    public void setupGame(Request clientRequest){
        Command command = new Command("Interfaces.IGamePlay", "setupGame",
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);
        Database.getInstance().getAllGameCommands().put(clientRequest.getGameId(), new ArrayList<Command>());
    }

    @Override
    public Result discardDestCards(Request clientRequest) {
        createCommand("discardDestCards", clientRequest);
        return null;
    }

    @Override
    public Result updateClient(Request request) {
        return null;
    }

    @Override //creates an addGameHistory command and adds it to the gameCommands list
    public Result addGameHistory(Request clientRequest){
        createCommand("addGameHistory", clientRequest);
        return null;
    }

    @Override
    public Result drawDestCards(Request clientRequest) {
        createCommand("drawDestCards", clientRequest);
        return null;
    }

    @Override
    public Result drawTrainCard(Request clientRequest) {
        createCommand("drawTrainCard", clientRequest);
        return null;
    }

    @Override
    public Result takeFaceUpCard(Request clientRequest) {
        createCommand("takeFaceUpCard", clientRequest);
        return null;
    }

    @Override
    public Result claimRoute(Request clientRequest) {
        createCommand("claimRoute", clientRequest);
        return null;
    }

    @Override
    public void endTurn(Request clientRequest) {
        createCommand("endTurn", clientRequest);
    }

    @Override
    public Result endGame(Request clientRequest) {
        createCommand("endGame", clientRequest);
        return null;
    }

    @Override
    public void dealFaceUpCards(Request clientRequest) {
        createCommand("shuffleFaceUp", clientRequest);
    }

    private void createCommand(String methodName, Request clientRequest){
        Command command = new Command("Interfaces.IGamePlay", methodName,
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });

        //adds the command to the gameCommands arraylist in database:
        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
    }


}
