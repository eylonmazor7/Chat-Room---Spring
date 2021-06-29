package com.example.ex4;

import com.example.ex4.beans.UserBeansA;
import com.example.ex4.beans.UsersOnlineA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;


/**
 * The type Bean configuration.
 * Determines which configuration level will be assigned for the beans.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Users online users online a.
     * @return the users online a
     */
    @Bean
    @ApplicationScope
    public UsersOnlineA usersOnline(){
        return new UsersOnlineA();
    }


    /**
     * User beans user beans a.
     * @return the user beans a
     */
    @Bean
    @SessionScope
    public UserBeansA userBeans () {
        return new UserBeansA();
    }
}
