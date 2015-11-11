package com.bionic.fp;

import com.bionic.fp.entity.Accounts;
import com.bionic.fp.service.AccountsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by boubdyk on 11.11.2015.
 */
public class Test {
    private static ApplicationContext context;
    private static AccountsService accountsService;

    static {
        context = new ClassPathXmlApplicationContext("beans.xml");
        accountsService = context.getBean(AccountsService.class);
    }

    public static void main(String[] args) {
        Accounts account = new Accounts();
        account.setActive(true);
        account.setEmail("xxx@xxx.com");
        account.setUserName("Lozhkin");
        System.out.println(account);
        System.out.println(accountsService.addAccount(account));
    }
}
