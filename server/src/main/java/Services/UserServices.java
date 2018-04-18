package Services;

import java.util.UUID;

import Interfaces.IServerUser;
import Models.Request;
import Models.Result;
import Models.User;
import Server.Database;

public class UserServices implements IServerUser {

    private static UserServices theOne = new UserServices();

    public static UserServices getInstance() {
        return theOne;
    }

    private UserServices() {}

    //Pass in username and password
    //Checks if user exists in database
    //Checks given password against existing password
    //returns authToken or Error message
    @Override
    public Result login(Request request){ //(String username, String password){
        String username = request.getUsername();
        String password = request.getPassword();

        Result response = new Result();
        User user = Database.getInstance().findUserByName(username);
        if(user != null)
        {
            if (password.equals(user.getPassword()))
            {
//                Database.getInstance().getClients().add(user.getAuthToken());
                request.setAuthToken(user.getAuthToken());
                Database.getInstance().storeUsers();

                //TODO: remove this TestClientServices line
//                TestClientServices.getInstance().createGame();
                response = LobbyServices.getInstance().updateClient(request);

                response.setAuthToken(user.getAuthToken());
                response.setSuccess(true);

                System.out.println(username+" logged in.");
            }
            else
            {
                response.setErrorMsg("ERROR: Incorrect username/password combination");
                response.setSuccess(false);
            }
        }
        else
        {
            response.setErrorMsg("ERROR: Incorrect username/password combination");
            response.setSuccess(false);
        }
        return response;
    }

    //Pass in username and password
    //Checks if user already exists in database
    //generates authToken
    //creates new user object
    //Inserts new user into database
    //returns authToken or Error message
    @Override
    public Result register(Request request){ //(String username, String password){
        String username = request.getUsername();
        String password = request.getPassword();

        Result response = new Result();
        if(!Database.getInstance().getUsers().containsKey(username))
        {
            String authToken = randomString();
            User user = new User(username, password, authToken);
            Database.getInstance().getUsers().put(username, user); //for login purposes
            Database.getInstance().getUsers().put(authToken, user); //for authentication purposes
//            Database.getInstance().getClients().add(authToken);

            Database.getInstance().storeUsers();

            request.setAuthToken(user.getAuthToken());
            response = LobbyServices.getInstance().updateClient(request);

            response.setAuthToken(authToken);
            response.setSuccess(true);

            System.out.println(username + " registered as a new user.");
        }
        else
        {
            response.setErrorMsg("ERROR: Invalid Registration");
            response.setSuccess(false);
        }
        return response;
    }

    private String randomString()
    {
        return UUID.randomUUID().toString();
    }
}
