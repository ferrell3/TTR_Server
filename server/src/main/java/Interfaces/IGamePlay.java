package Interfaces;

import Models.Request;
import Models.Result;

/**
 * Created by kiphacking on 3/3/18.
 */

public interface IGamePlay {

    void setupGame(Request request);
    Result addGameHistory(Request request);
    Result discardDestCards(Request request);
    Result updateClient(Request request);
    Result drawDestCard(Request request);
}
