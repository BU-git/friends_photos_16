package com.bionic.fp;

import com.bionic.fp.domain.AccountEvent;
import com.bionic.fp.domain.Role;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by boubdyk on 11.11.2015.
 */
public class Test {
    private static ApplicationContext context;
    private static AccountEventService accountEventService;

    static {
        context = new ClassPathXmlApplicationContext("spring/data.xml");
//        accountsService = context.getBean(AccountService.class);
        accountEventService = context.getBean(AccountEventService.class);
    }

    public static void main(String[] args) {
        //String fbID, String fbProfile, String fbToken, String userName
        //System.out.println(accountsService.loginByFB("58966666", "fb.com/fbfb", "tokkkken", "Tomas"));

        AccountEvent byAccountAndGroupId = accountEventService.getByAccountAndGroupId(1L, 1L);
        Role role = byAccountAndGroupId.getRole();

        boolean canChangeSettings = role.isCanChangeSettings();

        System.out.println(canChangeSettings);


    }
}
