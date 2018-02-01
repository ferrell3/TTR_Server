package Interfaces;

public interface IServerUser {
    //Pass in username and password
    //Checks if user exists in database
    //Checks given password against existing password
    //returns authToken or Error message
    String login(String username, String password);

    //Pass in username and password
    //Checks if user already exists in database
    //generates authToken
    //creates new User object
    //Inserts new user into database
    //returns authToken or Error message
    String register(String username, String password);
}
