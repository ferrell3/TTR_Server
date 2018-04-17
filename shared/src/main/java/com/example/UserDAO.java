package com.example;

import java.util.HashMap;

/**
 * Created by ferrell3 on 4/9/18.
 */

public interface UserDAO {
//    HashMap<String, User> getUsers();
//    void setUsers(HashMap<String, User> users);
//    void addUser(User user);
//    void removeUser(User user);
//    void storeUsers();
//    void loadUsers();

    void storeUsers(String jsonUsers);
    String loadUsers();
    void clear();
}
