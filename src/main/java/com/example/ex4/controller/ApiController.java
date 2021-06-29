package com.example.ex4.controller;

import com.example.ex4.DB.ChatInter;
import com.example.ex4.DB.ChatMessage;
import com.example.ex4.HttpHelpers.ReqMes;
import com.example.ex4.HttpHelpers.ResMes;
import com.example.ex4.beans.UserBeansA;
import com.example.ex4.beans.UsersOnlineA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Api controller.
 * this controller responsible of to update the DB, and send update to the users of who still online
 * and the last messages.
 */
@Controller
public class ApiController {

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
     * get users bean (manage the users list) - application bean
     */
    @Resource(name = "usersOnline")
    private UsersOnlineA userOnline;

    /**
     * user bean - session bean
     */
    @Resource(name = "userBeans")
    private UserBeansA userBean;

    /**
     * This function get the message, validate the message and check if the user still online, and if everything is
     * good - save the message in DB.
     * finally, the function return ReqMes object answer (message inside)
     *
     * @param mess the message from the user that he wants to send
     * @return the answer
     */
    @PostMapping("/addMessage")
    public @ResponseBody
    ResMes insert(@RequestParam String mess) {
        String userName = userBean.getUserName();

        if(userName == null || !userOnline.checkUser(userBean.getUserName()))
            return new ResMes("disconnected");

        if(mess.trim().equals(""))
            return new ResMes("message required");

        updateUserTime();
        getDB().save(new ChatMessage(userName, mess));

        return new ResMes("");
    }

    /**
     * Ret to search string.
     * this is a get request - and this function determines destination for this url.
     * the function disallowing to search via get request.
     * @param model the model
     * @return the url for the next page
     */
    @GetMapping(value="/searchText")
    public String retToSearch(Model model){
        if(!userOnline.checkUser(userBean.getUserName())){
            model.addAttribute("mess","Its look like you are not connected ,Please try login again.");
            return "login";
        }
        updateUserTime();
        return "redirect:/search";
    }


    /**
     * Get all list.
     * this function get the user request to search with type of search and the text itself.
     * the function get the data from the DB and send back answer.
     * @param reqMessage the req message
     * @param result     the result
     * @return the list with the results.
     */
    @PostMapping(value="/searchText")
    public @ResponseBody List<ChatMessage> getAll(@Valid @RequestBody ReqMes reqMessage, BindingResult result){
        if (result.hasErrors())
            return null;

        String text = reqMessage.getMessage().trim();
        updateUserTime();
        if(!text.equals("")){
            if(reqMessage.getSearchOptions().equals("user name"))
                return getDB().findAllByUserNameOrderByIdDesc(text);
            if(reqMessage.getSearchOptions().equals("free text"))
                return searchText(text);
        }
        return null;
    }

    /**
     * Move to string.
     * this is a get request - and this function determines destination for this url.
     * the function disallowing check for update via get request.
     * @param model the model with the message to the next page (if necessary)
     * @return the string
     */
    @GetMapping("/checkUpdate")
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
     * Check update list.
     * This function returns the last five messages after verifying the user is still online.
     * @return the list
     */
    @PostMapping(value="/checkUpdate")
    public @ResponseBody List<ChatMessage> checkUpdate(){
        List<ChatMessage> res = null;
        res = getDB().findTop5ByOrderByIdDesc();
        Collections.reverse(res);
        return res;
    }

    /**
     * Gets online users.
     * Returns a list with all connected users after verifying the user is still online.
     * @return the online users
     */
    @PostMapping(value ="/userUpdate")
    public @ResponseBody List<String> getOnlineUsers() {
        return userOnline.getAllOnlineUsers();
    }

    /**
     * This function checks if the wanted text find contained in the database
     * If so- the function will add the text to the returned list
     * @param text - The wanted text to find.
     * @return list with the search results.
     */
    private List<ChatMessage> searchText(String text) {
        List<ChatMessage> res = new ArrayList<ChatMessage>();
        for (ChatMessage chatMessage : getDB().findAll())
            if(chatMessage.getMessage().contains(text))
                res.add(chatMessage);
        return res;
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
