package com.bionic.fp.web.security.spring.config.stateless;

import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationStrategy;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationFilter;
import com.bionic.fp.web.security.spring.infrastructure.filter.impl.CookieHeaderTokenAuthenticationStrategy;
import com.bionic.fp.web.security.spring.infrastructure.filter.impl.CookieTokenAuthenticationStrategy;
import com.bionic.fp.web.security.spring.infrastructure.filter.impl.HeaderTokenAuthenticationStrategy;
import com.bionic.fp.web.security.spring.infrastructure.filter.impl.SessionAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bionic.fp.Constants.RestConstants.PATH.API;
import static com.bionic.fp.Constants.RestConstants.PATH.AUTH;
import static com.bionic.fp.Constants.RestConstants.PATH.V1;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Spring security configuration
 *
 * @author Sergiy Gabriel
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StatelessWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService;
    @Autowired private AuthenticationStrategy authenticationStrategy;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandlerBean() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                super.onAuthenticationFailure(request, response, exception);
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
            }
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPointBean() {
        return (request, response, authException) -> response.sendError(SC_UNAUTHORIZED, "Access Denied");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationFilter authTokenFilter = new AuthenticationFilter(this.authenticationStrategy);
        authTokenFilter.setAuthenticationManager(this.authenticationManagerBean());
        authTokenFilter.setAuthenticationFailureHandler(this.authenticationFailureHandlerBean());
        return authTokenFilter;
    }

//    @Bean
//    public AuthenticationStrategy authenticationStrategyBean() {
//        return new CookieTokenAuthenticationStrategy();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPointBean())
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(API+V1+AUTH+"/**").permitAll()
                .antMatchers(API+V1+"/**").authenticated()
                .anyRequest().permitAll();

        // Custom authentication
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    }

}
