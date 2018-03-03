package Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interfaces.IClient;
import Interfaces.ICommand;
import Models.Command;
import Models.Request;
import Server.Database;

public class ClientProxy implements IClient {

    private static ClientProxy theCP = new ClientProxy();

    public static ClientProxy getInstance() {
        return theCP;
    }

    private ClientProxy() {
    }

    //The server will add a new list or add to the list of commands in the LobbyServices
//    private Map<String,List<ICommand>> clientCommands; //Client authToken is the key String

    @Override //creates a command and adds it to the list for each client
    public void createGame(Request clientRequest){ //(String username, String gameId)
        Request request = new Request();
        request.setUsername(clientRequest.getUsername());
        request.setGameId(clientRequest.getGameId());

        Command command = new Command("Interfaces.ILobby", "createGame",
                new String[]{ "Models.Request" }, new Request[]{ request });

        //update master command list in database - for all clients to access:
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);
    }

    @Override
    public void joinGame(Request clientRequest){ //(String username, String gameId){
        Request request = new Request();
        request.setUsername(clientRequest.getUsername());
        request.setGameId(clientRequest.getGameId());

        Command command = new Command("Interfaces.ILobby", "joinGame",
                new String[]{ "Models.Request" }, new Request[]{ request });

        //update master command list in database - for all clients to access:
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);
    }

    @Override
    public void leaveGame(Request clientRequest) {
        Request request = new Request();
        request.setUsername(clientRequest.getUsername());
        request.setGameId(clientRequest.getGameId());

        Command command = new Command("Interfaces.ILobby", "leaveGame",
                new String[]{ "Models.Request" }, new Request[]{ request });

        //update master command list in database - for all clients to access:
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);
    }

    @Override
    public void startGame(Request clientRequest){ //(String gameId){
        Request request = new Request();
        request.setGameId(clientRequest.getGameId());
        Command command = new Command("Interfaces.ILobby", "startGame",
                new String[]{ "Models.Request" }, new Request[]{ request });

        //update master command list in database - for all clients to access:
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);
    }
}
