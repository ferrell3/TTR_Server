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
                    //populate the game with the chat strings
                    Database.getInstance().getGames().get(gameId).addChatMessage(chat.displayChat());
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

//    @Override
//    public Result getChats(Request request) {
//        String authToken = request.getAuthToken();
//        String gameId = request.getGameId();
//        String username = request.getUsername();
//
//        Result result = new Result();
//
//        try {
//            //check if requesting client is an active (logged in) client
//            if (Database.getInstance().getClients().contains(authToken)) {
//                //check if active game exists
//                if (!Database.getInstance().getActiveGames().containsKey(gameId)) {
//                    result.setSuccess(false);
//                    result.setErrorMsg("Please enter a valid game ID.");
//                    System.out.println("ERROR: in getChats() -- Empty gameID");
//                }
//                //check if gameId already exists
//                else{
//                    //update the game
//                    ChatProxy.getInstance().getChats(request);
//
//                    result.setChatMessages(returnList);
//                    result.setSuccess(true);
//
//                    System.out.println(username + " sent chat message");
//                }
//            } else {
//                result.setSuccess(false);
//                result.setErrorMsg("Invalid authorization token.");
//                System.out.println("ERROR: in getChats() -- Invalid auth token");
//            }
//            return result;
//        }catch(Exception e){
//            System.out.println(e);
//            result.setSuccess(false);
//            result.setErrorMsg("ERROR: getChats method failed");
//            return result;
//        }
//
//
//    }
}
