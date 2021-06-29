package com.example.ex4.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

/**
 * The type Users online a.
 * This object will be created only once, as the application start its running.
 * It will preform checks about the users and manage details
 */
@Component
public class UsersOnlineA implements Serializable {
    /**
     * This map will contain for each user its name and the last time the user preformed an action.
     */
    private Map<String, LocalTime> users = Collections.synchronizedMap(new HashMap<>());

    /**
     * Check user for login.
     * This function happens only via the login page.
     * It checks if the user name is available- in terms of username already in use or the user is disconnected.
     * @param user the user
     * @return true if the username is available, false otherwise.
     */
    public boolean checkUserForLogin(String user){
        if(users.containsKey(user) && !checkUser(user)){
            users.remove(user);
            return false;
        }
        return users.containsKey(user); //user already in use
    }

    /**
     * Insert user.
     * This function inserting a new user to the users map.
     * @param userName the s
     */
    public void insertUser(String userName){
        LocalTime now = LocalTime.now();
        users.put(userName,now);
    }

    /**
     * Update user time.
     * Updating the last time an action took place by the user.
     * @param userName the user name.
     * @param localTime the local time
     */
    public void updateUserTime(String userName, LocalTime localTime){
        users.put(userName,localTime);
    }

    /**
     * Check user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean checkUser(String user){
        if(!users.containsKey(user))
            return false;

        LocalTime now = LocalTime.now();
        Duration duration = Duration.between(users.get(user),now);
        return duration.getSeconds() <= 600;
    }

    /**
     * Get all online users list.
     * @return the list
     */
    public List<String> getAllOnlineUsers(){
        List<String> res = new ArrayList<>();
        LocalTime now = LocalTime.now();
        Duration duration;

        for (Map.Entry<String, LocalTime> entry : users.entrySet()) {
             duration = Duration.between(entry.getValue(),now);
             if(duration.getSeconds() <= 600)
                 res.add(entry.getKey());
        }
        return res;
    }

    /**
     * Delete a user.
     * @param userName the userName
     */
    public void deleteUser(String userName){
        users.remove(userName);
    }

    /**
     * Gets users.
     * @return the users
     */
    public Map<String, LocalTime> getUsers() {
        return users;
    }

    /**
     * Sets users.
     * @param users the users
     */
    public void setUsers(Map<String, LocalTime> users) {
        this.users = users;
    }
}
