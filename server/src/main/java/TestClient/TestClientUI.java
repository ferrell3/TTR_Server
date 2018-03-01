package TestClient;

import java.util.Scanner;

import Models.Command;
import Models.Request;
import Models.Result;

/**
 * Created by ferrell3 on 2/6/18.
 */

public class TestClientUI {
    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);

        //TEST LOGIN
        Request loginRequest = new Request();
        loginRequest.setUsername("jordan");
        loginRequest.setPassword("jf");
        Command loginCommand = new Command("Interfaces.IServerUser", "login",
                new String[]{ "Models.Request" }, new Request[]{ loginRequest });


        Result result = TestClientCommunicator.getInstance().sendCommand(loginCommand);

        if (result.isSuccessful())
        {
            System.out.println("Login successful!");
            System.out.println(result.getAuthToken());
        }
        else
        {
            System.out.println("Error:");
            System.out.println(result.getErrorMsg());
        }
        System.out.println();
        //END LOGIN TEST

        //TEST REGISTER
        Request registerRequest = new Request();
        registerRequest.setUsername("chipper");
        registerRequest.setPassword("tacos");
        Command registerCommand = new Command("Interfaces.IServerUser", "register",
                new String[]{ "Models.Request" }, new Request[]{ registerRequest });

        Result regResult = TestClientCommunicator.getInstance().sendCommand(registerCommand);

        if (regResult.isSuccessful())
        {
            System.out.println("Registration successful!");
            System.out.println(regResult.getAuthToken());
        }
        else
        {
            System.out.println("Error:");
            System.out.println(regResult.getErrorMsg());
        }
        System.out.println();
        //END REGISTER TEST

        //TEST CREATE GAME
        Request gameRequest = new Request();
        gameRequest.setAuthToken("a1fb6d30-51e7-4669-b944-120989aefb06");
        gameRequest.setGameId("Jordan's Game");
        Command gameCommand = new Command("Interfaces.ILobby", "createGame",
                new String[]{ "Models.Request" }, new Request[]{ gameRequest });

        Result gameResult = TestClientCommunicator.getInstance().sendCommand(gameCommand);

        if (gameResult.isSuccessful())
        {
            System.out.println(gameRequest.getGameId() + " successfuly created!");
        }
        else
        {
            System.out.println("Error:");
            System.out.println(gameResult.getErrorMsg());
        }
        //END CREATE GAME TEST
        System.out.println();

//        while (true)
//        {
//            System.out.println("Welcome to Phase 0.5! Prepare to have your strings processed! (Enter Q to quit)");
//            System.out.print("Enter a method to process: ");
//            String type = in.nextLine().toLowerCase().trim();
//            if(type.equals("q")) { break; }
////            else if(!type.equals("parseinteger")
////                    && !type.equals("trim")
////                    && !type.equals("tolowercase"))
////            {
////                System.out.println("Sorry, that's not a valid suffix. Valid options include: toLowerCase, trim, and parseInteger.");
////            }
//            else
//            {
//                if(type.equals("parseinteger") || type.equals("parse")){
//                    type = "parseInteger";
//                }
//                else if(type.equals("tolowercase") || type.equals("lower"))
//                {
//                    type = "toLowerCase";
//                }
//                System.out.print("Enter the string: ");
//
//                String input = in.nextLine();
//                if(input.toLowerCase().equals("q")) { break; }
//
//                Command command = new Command("Interfaces.IStringProcessor", type,
//                        new String[]{ "Models.Request" }, new Request[]{new Request(input)});
//
////                GenericCommand move = new GenericCommand("VideoGame", "move",
////                        new Class<?>[]{ int.class, Request.class },
////                        new Object[] { 3 , new Location(75, 12) });
//
//                Result result = TestClientCommunicator.getInstance().sendCommand(command);
//
//                if (result.isSuccessful())
//                {
//                    System.out.println(result.getData());
//                }
//                else
//                {
//                    System.out.println(result.getErrorMsg());
//                }
//                System.out.println();
//            }
//        }
    }
}
