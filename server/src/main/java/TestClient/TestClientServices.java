package TestClient;

import Models.Request;
import Server.Database;
import Services.LobbyServices;

public class TestClientServices {
    private static TestClientServices theOne = new TestClientServices();

    public static TestClientServices getInstance() {
        return theOne;
    }

    private TestClientServices() {}

//        User u1 = new User("jordan", "aa", "a1fb6d30-51e7-4669-b944-120989aefb06");
//        User u2 = new User("kip","aa","1fee61ae-d871-4548-8fba-a775dab78f8b");
//        User u3 = new User("brian","aa","01b7cb2c-24c1-4c82-8f6f-c6ee8ab39d2e");
//        User u4 = new User("finn","aa", "82f90744-ef61-4298-84ce-3070dfc25137");

    public void createGame() {
        if(!Database.getInstance().getGames().containsKey("gameTEST"))
        {
            Request newReq = new Request();
            newReq.setAuthToken("a1fb6d30-51e7-4669-b944-120989aefb06"); //jordan
            newReq.setGameId("gameTEST");
            LobbyServices.getInstance().createGame(newReq);

            newReq.setAuthToken("1fee61ae-d871-4548-8fba-a775dab78f8b"); //kip
            LobbyServices.getInstance().joinGame(newReq);

//            newReq.setAuthToken("82f90744-ef61-4298-84ce-3070dfc25137"); //finn
//            LobbyServices.getInstance().joinGame(newReq);
//
//            newReq.setAuthToken("01b7cb2c-24c1-4c82-8f6f-c6ee8ab39d2e"); //brian
//            LobbyServices.getInstance().joinGame(newReq);

        }
    }
}
