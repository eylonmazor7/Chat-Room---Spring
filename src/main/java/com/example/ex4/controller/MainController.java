package com.example.ex4.controller;

import com.example.ex4.DB.ChatInter;
import com.example.ex4.DB.ChatMessage;
import com.example.ex4.beans.UserBeansA;
import com.example.ex4.beans.UsersOnlineA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * The type Main controller.
 * this controller responsible to manage the user navigation between pages, and get new users.
 */
@Controller
public class MainController {

    /**
     * user bean - session bean
     */
    @Resource(name = "userBeans")
    private UserBeansA userBean;

    /**
     * get users bean (manage the users list) - application bean
     */
    @Resource(name = "usersOnline")
    private UsersOnlineA userOnline;

    /**
     * get DB interface.
     * get function to get access to DB.
     */
    @Autowired
    private ChatInter dataBase;

    /**
     * get function for the database.
     * @return access to database
     */
    private ChatInter getDB() { return dataBase; }


    /**
     * Main string.
     * This function check if the user is already online - and redirect him to the chat room page
     * @return the login page if the user is new, otherwise to the chat room page
     */
    @GetMapping("/")
    public String main() {
        String userName = userBean.getUserName();
        if(userName != null) { //if the session exist
            if (userOnline.checkUser(userName)){
                updateUserTime();
                return "redirect:/chat";
            }
            else
                userBean.setUserName(null); //need to delete from the list
        }

        return "login";
    }

    /**
     * This function get the insertUser GET request and check if the user online or not.
     * the function disallowing to register via get request.
     * @param model for pass data the HTML page
     * @return the next page (if the user online -> chat page, otherwise -> home page)
     */
    @GetMapping("/insertUser")
    public String moveTo(Model model){
        if(userOnline.checkUser(userBean.getUserName())) {
            updateUserTime();
            model.addAttribute("userName", userBean.getUserName());
            model.addAttribute("last5", getDB().findTop5ByOrderByIdDesc());
            model.addAttribute("onlineUsers", userOnline.getAllOnlineUsers());
            return "redirect:/chat";
        }
        model.addAttribute("mess","Its look like you are not connected ,Please try login again.");
        return "forward:/";
    }

    /**
     * this function register a new user, after validation of the name of the user, and if the name available or not.
     * @param userName           the user name
     * @param model              the model
     * @return the string
     */
    @PostMapping("/insertUser")
    public String moveTo(@RequestParam String userName, Model model){
        if(userBean.getUserName() != null) //already login
           return "redirect:/chat";

        userName = userName.trim();

        if(userName.equals("")){ //invalid message
            model.addAttribute("mess","Name is required");
            return "login";
        }
        if(userOnline.checkUserForLogin(userName)) { //userName not available
            model.addAttribute("mess", "This User Name is already in use.");
            return "login";
        }

        ChatMessage lastMessageInDB = getDB().findTopByOrderByIdDesc();
        if(lastMessageInDB == null) //first user to login get a "welcome message"
            getDB().save(new ChatMessage("Dor & Eylon", "Welcome to our chat room"));

        userOnline.insertUser(userName);
        userBean.setUserName(userName);

        model.addAttribute("userName", userName);
        return "redirect:/chat";
    }

    /**
     * this function handle a GET request for enter to the chat room.
     * the function check if the user still online before go ahead to the chat room.
     * the function add the last 5 messages (and reverse them before the sending) to the model for the thymeleaf
     * @param model the model
     * @return the chat room page
     */
    @GetMapping("/chat")
    public String getChat(Model model) {
        if(!userOnline.checkUser(userBean.getUserName())){
            model.addAttribute("mess","Its look like you are not connected ,Please try login again.");
            return "login";
        }

        updateUserTime();
        List<ChatMessage> res = null;
        res = getDB().findTop5ByOrderByIdDesc();
        Collections.reverse(res);

        model.addAttribute("userName", userBean.getUserName());
        model.addAttribute("last5", res);
        model.addAttribute("onlineUsers", userOnline.getAllOnlineUsers());
        return "chat";
    }

    /**
     * this function handle a GET request for enter to the search page.
     * @param model the model
     * @return the search page if the user still online
     */
    @GetMapping("/search")
    public String search(Model model){
        if(!userOnline.checkUser(userBean.getUserName())){
            model.addAttribute("mess","Its look like you are not connected ,Please try login again.");
            return "login";
        }
        updateUserTime();

        return "search";
    }

    /**
     * Log out string.
     * this function deletes the user and removes it from the map in case the user is connected.
     * @return the string
     */
    @PostMapping("/logOut")
    public String logOut(){
        if(userOnline.checkUser(userBean.getUserName())){
            userOnline.deleteUser(userBean.getUserName());
            userBean.setUserName(null);
        }
        return "login";
    }

    /**
     * This function will update the last relevant time for the user.
     */
    private void updateUserTime(){
        String user = userBean.getUserName();
        LocalTime now = LocalTime.now();
        userOnline.updateUserTime(user, now);
    }
}