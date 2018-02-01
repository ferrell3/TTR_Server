package Services;

import java.util.UUID;

import Interfaces.IServerUser;
import Models.User;
import Server.Database;

public class UserServices implements IServerUser {
    //Pass in username and password
    //Checks if user exists in database
    //Checks given password against existing password
    //returns authToken or Error message
    @Override
    public String login(String username, String password){
        String response = "ERROR: Incorrect username/password combination";
        User user = Database.getInstance().findUserByName(username);
        if(user != null)
        {
            if (password.equals(user.getPassword()))
            {
                response = user.getToken();
            }
        }
        //Do we want to start every error message with "ERROR"?
        //TODO: Decide how we want to indicate an error vs a token
        return response;
    }

    //Pass in username and password
    //Checks if user already exists in database
    //generates authToken
    //creates new user object
    //Inserts new user into database
    //returns authToken or Error message
    @Override
    public String register(String username, String password){
        String response = "ERROR: Invalid Username";
        if(!Database.getInstance().getUsers().containsKey(username))
        {
            //Do we generate the authToken here?
            //Where else do we want to generate it?
            String authToken = randomString();
            User user = new User(username, password, authToken);
            Database.getInstance().getUsers().put(username, user); //for login purposes
            Database.getInstance().getUsers().put(authToken, user); //for authentication purposes
            response = authToken;
        }

        return response;
    }

    public String randomString()
    {
        return UUID.randomUUID().toString();
    }
}
