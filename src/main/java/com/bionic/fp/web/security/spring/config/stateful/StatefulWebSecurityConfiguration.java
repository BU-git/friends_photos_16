package com.bionic.fp.web.security.spring.config.stateful;

import com.bionic.fp.Constants;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationFilter;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationStrategy;
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
import static com.bionic.fp.Constants.RestConstants.REST_API_VERSION;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Spring security configuration
 *
 * @author Sergiy Gabriel
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StatefulWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsService userDetailsService;

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
        AuthenticationFilter authTokenFilter = new AuthenticationFilter(this.authenticationStrategyBean());
        authTokenFilter.setAuthenticationManager(this.authenticationManagerBean());
        authTokenFilter.setAuthenticationFailureHandler(this.authenticationFailureHandlerBean());
        return authTokenFilter;
    }

    @Bean
    public AuthenticationStrategy authenticationStrategyBean() {
        return new SessionAuthenticationStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPointBean())
            .and()
            .sessionManagement()
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(API+REST_API_VERSION+AUTH+"/**").permitAll()
                .antMatchers(API+REST_API_VERSION+"/**").authenticated()
                .anyRequest().permitAll();

        // Custom authentication
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    }

}
