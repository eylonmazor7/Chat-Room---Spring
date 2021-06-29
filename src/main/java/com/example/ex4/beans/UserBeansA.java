package com.example.ex4.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * The type User beans a.
 * Every time a session created for a user, this object will be created.
 */
@Component
public class UserBeansA implements Serializable {
    /**
     * User name.
     */
    private String userName;

    /**
     * Gets user name.
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     * @param userNam the user name
     */
    public void setUserName(String userNam) {
        userName = userNam;
    }
}
