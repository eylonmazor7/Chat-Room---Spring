package com.example.ex4.HttpHelpers;

/**
 * The type Res mes.
 * Created for each message sent to the user.
 */
public class ResMes {
    /**
     * The message to send.
     */
    private String responseMessage;

    /**
     * Instantiates a new Res mes.
     * @param userName the userName
     */
    public ResMes(String userName) {
        this.responseMessage = userName;
    }
}
