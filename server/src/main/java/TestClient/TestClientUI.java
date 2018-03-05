package TestClient;

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

        //TEST LOGIN
        Request loginRequest2 = new Request();
        loginRequest2.setUsername("kip");
        loginRequest2.setPassword("kh");
        Command loginCommand2 = new Command("Interfaces.IServerUser", "login",
                new String[]{ "Models.Request" }, new Request[]{ loginRequest2 });


        Result result2 = TestClientCommunicator.getInstance().sendCommand(loginCommand2);

        if (result2.isSuccessful())
        {
            System.out.println("Login successful!");
            System.out.println(result2.getAuthToken());
        }
        else
        {
            System.out.println("Error:");
            System.out.println(result2.getErrorMsg());
        }
        System.out.println();
        //END LOGIN TEST


        // TEST JOIN GAME
        Request joinRequest = new Request();
        //Kip's authToken
        joinRequest.setAuthToken("a1fb6d30-51e7-4669-b944-120989aefb06");
        joinRequest.setGameId("hello");

        Command joinCmd = new Command("Interfaces.ILobby", "joinGame",
                new String[]{ "Models.Request" }, new Request[]{ joinRequest });

        Result result3 = TestClientCommunicator.getInstance().sendCommand(joinCmd);

        if (result3.isSuccessful())
        {
            System.out.println("Join successful!");
        }
        else
        {
            System.out.println("Error:");
            System.out.println(result3.getErrorMsg());
        }
        System.out.println();


//        // TEST START GAME
//        Request startRequest = new Request();
//        startRequest.setAuthToken("1fee61ae-d871-4548-8fba-a775dab78f8b");
//        startRequest.setGameId("Jordan's Game");
//        Command startCmd = new Command("Interfaces.ILobby", "startGame",
//                new String[]{ "Models.Request" }, new Request[]{ startRequest });
//
//        Result result4 = TestClientCommunicator.getInstance().sendCommand(startCmd);
//
//        if (result4.isSuccessful())
//        {
//            System.out.println("Start game successful!");
//        }
//        else
//        {
//            System.out.println("Error:");
//            System.out.println(result4.getErrorMsg());
//        }
//        System.out.println();


        //  CHAT
        Request chatRequest = new Request();
        chatRequest.setAuthToken("a1fb6d30-51e7-4669-b944-120989aefb06");
        chatRequest.setGameId("Jordan's Game");
        chatRequest.setChatMessage("Hi its Kip!");
        Command chatCmd = new Command("Interfaces.IChat", "addChat",
                new String[]{ "Models.Request" }, new Request[]{ chatRequest });

        Result chatResult = TestClientCommunicator.getInstance().sendCommand(chatCmd);

        if (chatResult.isSuccessful())
        {
            System.out.println("Chat successfully added!");
        }
        else
        {
            System.out.println("Error:");
            System.out.println(chatResult.getErrorMsg());
        }
        System.out.println();

//        Result joinResult = ClientCommunicator.getInstance().sendCommand(joinCmd);

//        System.out.println();


//        temp = Client.getInstance().getCommandNum();
//        joinRequest.setCommandNum(temp);
//        joinGame(joinRequest);
        //END JOIN GAME TEST

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
