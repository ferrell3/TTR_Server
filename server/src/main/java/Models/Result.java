package Models;

import java.util.List;

import Interfaces.ICommand;

public class Result {
    //We don't need this class but it might make serializing easier also, error response checking

    //We don't necessarily need these variables, these are just some possible ideas
    private String errorMsg;
    private String token; //user authToken
    private String id; //game ID
    private boolean success;

    //we can have a list of commands, but it might be easier to just send back the info to the requesting client
    //we can also use this to send commands to the other polling clients though
    //I don't know if that would be easier
    private List<ICommand> commands; //list of client commands?

}
