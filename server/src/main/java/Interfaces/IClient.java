package Interfaces;

import Models.Request;

/**
 * Created by ferrell3 on 2/5/18.
 */

public interface IClient {
    void createGame(Request request); //(String username, String gameId);
    void joinGame(Request request); //(String username, String gameId);
    void startGame(Request request); //(String gameId);
}
