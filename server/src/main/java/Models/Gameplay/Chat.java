package Models.Gameplay;

public class Chat {
    private String username;
    private String message;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String displayChat(){

        return username + ": "+ message;

    }
}
