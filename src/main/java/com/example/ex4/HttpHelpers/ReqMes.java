package com.example.ex4.HttpHelpers;

/**
 * The type Req mes.
 * Every user request for a text search create an instance of this object
 */
public class ReqMes {
    /**
     * searchOptions- search by free text/user name
     */
    private final String searchOptions;
    /**
     * the text to find
     */
    private final String message;

    /**
     * Instantiates a new Req mes.
     * @param searchOptions the search options
     * @param message       the message
     */
    public ReqMes(String searchOptions, String message) {
        this.searchOptions = searchOptions;
        this.message = message;
    }

    /**
     * Gets search options.
     * @return the search options
     */
    public String getSearchOptions() {
        return searchOptions;
    }

    /**
     * Gets message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
