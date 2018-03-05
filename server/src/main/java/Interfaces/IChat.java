package Interfaces;

import Models.Request;
import Models.Result;

/**
 * Created by kiphacking on 3/2/18.
 */

public interface IChat {

    Result addChat(Request request);        //authtoken, gameId, and message

}
