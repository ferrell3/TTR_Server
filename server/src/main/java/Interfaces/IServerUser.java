package Interfaces;

import Models.Request;
import Models.Result;

public interface IServerUser {
    //Pass in username and password
    //Checks if user exists in database
    //Checks given password against existing password
    //returns authToken or Error message
    Result login(Request request); //(String username, String password);

    //Pass in username and password
    //Checks if user already exists in database
    //generates authToken
    //creates new User object
    //Inserts new user into database
    //returns authToken or Error message
    Result register(Request request); //(String username, String password);
}
