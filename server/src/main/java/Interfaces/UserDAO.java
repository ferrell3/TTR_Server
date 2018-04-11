package Interfaces;

import java.util.HashMap;

import Models.User;

/**
 * Created by ferrell3 on 4/9/18.
 */

public interface UserDAO {
    HashMap<String, User> getUsers();
    void setUsers(HashMap<String, User> users);
    void addUser(User user);
    void removeUser(User user);
    void storeUsers();
    void loadUsers();
}
