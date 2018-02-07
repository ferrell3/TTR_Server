package Interfaces;

import Models.Request;
import Models.Result;

public interface IServerGame {
    //for joinGame, the client might want to include the ability to add other usernames to the game
    //the UML said + joinGame(String token, String gameId):boolean, but maybe we call token something else
    //so it makes more sense...

    Result createGame(Request request); //(String authToken, String gameId);

    Result joinGame(Request request); //(String authToken, String gameId);

    Result startGame(Request request); //(String authToken, String gameId);

    Result updateClient(Request request); //(String authToken);


}
