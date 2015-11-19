package com.bionic.fp.rest;

import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.GroupType;
import com.bionic.fp.rest.dto.GroupCreateDTO;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.service.GroupService;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class SimpleIT {

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        this.groupService.clear();
    }

    @Test
    public void testSuccess() {
        assertTrue(this.groupService != null);
        assertTrue(this.accountService != null);
        assertTrue(this.context != null);

        Account owner = new Account("yaya@gmail.com", "Yaya", "yaya");
        owner.setActive(true);
        owner.setGuest(false);
        Long ownerId = this.accountService.addAccount(owner);

    }
}