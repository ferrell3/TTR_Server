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

/**
 * Created by ferrell3 on 2/5/18.
 */

public class ClientProxy implements IClient {

    private static ClientProxy theCP = new ClientProxy();

    public static ClientProxy getInstance() {
        return theCP;
    }

    private ClientProxy() {
        clientCommands = new HashMap<>();
    }

    //The server will add a new list or add to the list of commands in the GameServices
    private Map<String,List<ICommand>> clientCommands; //Client authToken is the key String

    @Override //creates a command and adds it to the list for each client
    public void createGame(Request clientRequest){ //(String username, String gameId)
        Request request = new Request();
        request.setUsername(clientRequest.getUsername());
        request.setGameId(clientRequest.getGameId());


        Command command = new Command("Interfaces.IServerGame", "createGame",
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

        Command command = new Command("Interfaces.IServerGame", "joinGame",
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
        Command command = new Command("Interfaces.IServerGame", "startGame",
                new String[]{ "Models.Request" }, new Request[]{ request });

        //update master command list in database - for all clients to access:
        ArrayList<Command> temp = Database.getInstance().getMasterCommandList();
        temp.add(command);
        Database.getInstance().setMasterCommandList(temp);

    }

    public Map<String, List<ICommand>> getClientCommands() {
        return clientCommands;
    }

    public void setClientCommands(Map<String, List<ICommand>> clientCommands) {
        this.clientCommands = clientCommands;
    }

    private void updateCommands(String authToken, ICommand command){


        //        for(String clientToken : Database.getInstance().getClients())
//        {
//            //for all other clients (skip the requested client)
//            if(!clientToken.equals(authToken))
//            {
//                //check if the CP already has a list of commands for the client
//                if(clientCommands.containsKey(authToken))
//                {
//                    //it does, add command to existing list
//                    clientCommands.get(clientToken).add(command);
//                }
//                else //does not have a list for this client yet
//                {
//                    //create a list
//                    ArrayList<ICommand> commandList = new ArrayList<>();
//                    //add command to list
//                    commandList.add(command);
//                    //add list to the map of client commands
//                    clientCommands.put(clientToken, commandList);
//                }
//            }
//        }
    }
}
