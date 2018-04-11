package Interfaces;

import Models.Request;
import Models.Result;

public interface IGamePlay {

    void setupGame(Request request);
    Result addGameHistory(Request request);
    Result discardDestCards(Request request);
    Result updateClient(Request request);
    Result drawDestCards(Request request);
    Result takeFaceUpCard(Request request);
    Result drawTrainCard(Request request);
    Result claimRoute(Request request);
    void endTurn(Request request);
    Result endGame(Request request);
    void dealFaceUpCards(Request request);
    Result rejoinGame(Request request);
}
