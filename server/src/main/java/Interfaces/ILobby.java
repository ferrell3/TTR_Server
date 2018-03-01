package Interfaces;

import Models.Request;
import Models.Result;

public interface ILobby {
    Result createGame(Request request); //(String authToken, String gameId);

    Result joinGame(Request request); //(String authToken, String gameId);

    Result startGame(Request request); //(String authToken, String gameId);

    Result updateClient(Request request); //(String authToken);


}
