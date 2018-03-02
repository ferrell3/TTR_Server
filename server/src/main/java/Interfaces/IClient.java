package Interfaces;

import Models.Request;

public interface IClient {
    void createGame(Request request); //(String username, String gameId);
    void joinGame(Request request); //(String username, String gameId);
    void leaveGame(Request request); //String username, String gameId);
    void startGame(Request request); //(String gameId);
}
