package Services;

import Interfaces.IChat;
import Models.Gameplay.Chat;
import Models.Request;
import Models.Result;
import Server.Database;

/**
 * Created by kiphacking on 3/2/18.
 */

public class ChatServices implements IChat{

    private static ChatServices theCS = new ChatServices();

    public static ChatServices getInstance() {
        return theCS;
    }

    private ChatServices() {}

    @Override
    public Result addChat(Request request) {
        String authToken = request.getAuthToken();
        String gameId = request.getGameId();
        String username = Database.getInstance().getUsername(authToken);
        String message = request.getChatMessage();

        Chat chat = new Chat();
        chat.setMessage(message);
        chat.setUsername(username);
        request.setChat(chat);
        Result result = new Result();

        try {
            // Check if requesting client is an active (logged in) client
            if (Database.getInstance().getClients().contains(authToken)) {

                // Check if game doesn't exist, return error
                if (!Database.getInstance().getGames().containsKey(gameId)) {
                    result.setSuccess(false);
                    result.setErrorMsg("addChat: Please enter a valid game ID.");
                    System.out.println("ERROR: in addChat() -- Empty gameID.");
                }

                // Else: game exists, add chat to database and create cmd object
                else{
                    // Populate the game with the chat strings
                    Database.getInstance().getGames().get(gameId).addChatMessage(chat.displayChat());

                    // Create cmdOject for chat object
                    ChatProxy.getInstance().addChat(request);

                    result.setSuccess(true);
                    System.out.println(username + " added chat: " + "\"" + chat.displayChat() +"\"");
                }
            } else {
                result.setSuccess(false);
                result.setErrorMsg("addChat: Invalid authorization token.");
                System.out.println("ERROR: in addChat() -- Invalid auth token");
            }
            return result;
        }catch(Exception e){
            System.out.println(e);
            result.setSuccess(false);
            result.setErrorMsg("ERROR: addChat method failed");
            return result;
        }


    }

}
