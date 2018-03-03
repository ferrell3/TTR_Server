package Services;

import Interfaces.IChatProxy;
import Models.Command;
import Models.Request;
import Server.Database;

/**
 * Created by kiphacking on 3/2/18.
 */

public class ChatProxy implements IChatProxy {

    private static ChatProxy theCP = new ChatProxy();

    public static ChatProxy getInstance() {
        return theCP;
    }

    private ChatProxy() {
//        clientCommands = new HashMap<>();
    }

    @Override //creates a command and adds it to the list for each client
    public void addChat(Request clientRequest){ //(String username, String gameId)

        Command command = new Command("Interfaces.IChat", "addChat",
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });

        //adds the command to the gameCommands list:
        Database.getInstance().getGames().get(clientRequest.getGameId()).addGameCommand(command);
    }
}
