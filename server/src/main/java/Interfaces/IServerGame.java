package Interfaces;

public interface IServerGame {
    //for joinGame, the client might want to include the ability to add other usernames to the game
    //the UML said + joinGame(String token, String gameId):boolean, but maybe we call token something else
    //so it makes more sense...

    String createGame(String authToken, String gameId);


    //TODO decide if we are passing in username or authtoken
    String joinGame(String username, String gameId);

    String startGame(String authToken, String gameId);

    String updateClient(String authToken);


}
