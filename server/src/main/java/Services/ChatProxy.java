package Services;

import Interfaces.IChatProxy;
import Models.Command;
import Models.Request;
import Server.Database;

/**
 * Created by kiphacking on 3/2/18.
 */

public class ChatProxy implements IChatProxy {

    private static ChatProxy theChP = new ChatProxy();

    public static ChatProxy getInstance() {
        return theChP;
    }

    private ChatProxy() {
//        clientCommands = new HashMap<>();
    }

    @Override //creates a command and adds it to the gameCommands list
    public void addChat(Request clientRequest){

        Command command = new Command("Interfaces.IChat", "addChat",
                new String[]{ "Models.Request" }, new Request[]{ clientRequest });

        //adds the command to the gameCommands arraylist in database:
        Database.getInstance().addGameCommand(clientRequest.getGameId(), command);
    }
}
