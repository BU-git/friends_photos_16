package com.bionic.fp.web.rest.v1;

import com.bionic.fp.AbstractIT;
import com.bionic.fp.domain.Account;
import com.bionic.fp.web.security.spring.infrastructure.User;
import com.bionic.fp.web.security.spring.infrastructure.utils.TokenUtils;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

/**
 * This is an integration test that verifies {@link CommentController}
 *
 * @author Sergiy Gabriel
 */
@ContextConfiguration(value = {
        "classpath:spring/test-root-context.xml",
        "classpath:spring/test-stateless-header-spring-security.xml"})
public class CommentRestControllerTest extends AbstractIT {

    @Resource private FilterChainProxy springSecurityFilterChain;
    @Resource private TokenUtils tokenUtils;
    @Value("${token.header}") private String tokenHeader;

    @Override
    @Before
    public void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build());
    }

    @Test
    public void testGetCommentsByEventSuccess() throws Exception {
    }

    @Test
    public void testGetCommentIdsByEventSuccess() throws Exception {

    }

    @Test
    public void testGetCommentsByPhotoSuccess() throws Exception {

    }

    @Test
    public void testGetCommentIdsByPhotoSuccess() throws Exception {

    }

    private String getToken(final Account account) {
        return this.tokenUtils.generateToken(new User(account));
    }
}