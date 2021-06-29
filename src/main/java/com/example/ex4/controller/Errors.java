package com.example.ex4.controller;

import com.example.ex4.beans.UserBeansA;
import com.example.ex4.beans.UsersOnlineA;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.time.LocalTime;

/**
 * The type Errors.
 * The controller catches wrong URLs.
 */
@Controller
public class Errors implements ErrorController {

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
     * Error string.
     * This function catches the wrong URLs and redirecting the user by its status.
     * @param model the model
     * @return the string
     */
    @RequestMapping("/error")
    public String error(Model model){
        String userName = userBean.getUserName();
        if(userName == null)
            model.addAttribute("mess","Wrong URL ,Please try login.");

        else if(!userOnline.checkUser(userBean.getUserName()))
            model.addAttribute("mess","Wrong url, and its look like you are disconnected, " +
                    "Please try login again and insert valid url.");

        else {
            updateUserTime();
            return "redirect:/chat";
        }
        return "forward:/";
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
