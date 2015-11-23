package com.bionic.fp.dao;

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

import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:spring/test-root-context.xml")
public class EventDAOTest {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private AccountEventDAO accountEventDAO;

//    @Autowired
//    private WebApplicationContext context;
//
//    @Before
//    public void setUp() {
//        RestAssuredMockMvc.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }

    @Test
    public void testInjectSuccess() throws Exception {
    }
}