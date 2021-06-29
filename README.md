# Chat-Room-Spring
Chat Room - Java Spring

In this exercise we have been asked to build some sort of a chat website. The welcome page will contain a text box where the user will enter its username. Every try to put a URL which is wrong or belong to a different page will cause a redirecting to the login page. After entering a username which is not currently in use, the user will be forwarded to the chat page. The chat will present the last five messages sent by the users. The page will also contain a list of current online users. An inactive user (for 600 seconds) will be automatically disconnected and sent back to login page.

The last five messages and the active users list will be updated every ten seconds. Every user could send a message at the box displayed at the page.

There will be a link to the search page, which will contain two searching options- by username and by text. We have used Thymeleaf to update the html page when loading the page. There's a use of an SQL database for storing and pulling data (messages and usernames).

Two kinds of Beans took place in our project -

Application level Bean- used to manage the list of users and update it. Will be created only once, when the application start its running.
Session level Bean- used to represent a user.
