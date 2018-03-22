package Interfaces;

import Models.Request;
import Models.Result;

public interface IChat {
    Result addChat(Request request);        //authtoken, gameId, and message
}
