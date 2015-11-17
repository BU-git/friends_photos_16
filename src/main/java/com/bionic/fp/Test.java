package com.bionic.fp;

import com.bionic.fp.entity.Account;
import com.bionic.fp.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */
public class Test {
    private static ApplicationContext context;
    private static AccountService accountsService;

    static {
        context = new ClassPathXmlApplicationContext("beans.xml");
        accountsService = context.getBean(AccountService.class);
    }

    public static void main(String[] args) {
        //String fbID, String fbProfile, String fbToken, String userName
        System.out.println(accountsService.loginByFB("58966666", "fb.com/fbfb", "tokkkken", "Tomas"));
    }
}
