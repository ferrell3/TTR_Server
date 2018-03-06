package Interfaces;

import Models.Request;
import Models.Result;

/**
 * Created by kiphacking on 3/3/18.
 */

public interface IGame {

    void setupGame(Request request);
    Result addGameHistory(Request request);
    Result discardDestCards(Request request);
}
