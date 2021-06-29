package com.example.ex4.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;


/**
 * The type Chat message.
 * The Entity annotation represent work with a database.
 */
@Entity
public class ChatMessage {
    /**
     * id- the user's id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * the user's user name.
     */
    @NotEmpty(message = "Name is mandatory")
    private String userName;

    /**
     * the message recieved from the user,
     */
    @NotEmpty(message = "message is mandatory")
    private String message;

    /**
     * Instantiates a new Chat message.
     */
    public ChatMessage(){}

    /**
     * Instantiates a new Chat message.
     * @param userName the user name
     * @param message  the message
     */
    public ChatMessage(String userName, String message){
        this.message = message;
        this.userName = userName;
    }

    /**
     * @return the user id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * to string function
     * @return string
     */
    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", userName=" + this.userName + ", message=" + this.message + '}';
    }
}



