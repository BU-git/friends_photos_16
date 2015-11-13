package com.bionic.fp;

import com.bionic.fp.entity.Account;
import com.bionic.fp.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        Account account = new Account();
        account.setActive(true);
        account.setEmail("xxx@xxx.com");
        account.setUserName("Lozhkin");
        System.out.println(account);
        System.out.println(accountsService.addAccount(account));
    }
}
