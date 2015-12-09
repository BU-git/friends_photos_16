package com.bionic.fp.security.filter;

import com.bionic.fp.security.SessionUtils;
import com.bionic.fp.service.AccountService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

/**
 * This filter allows only authorized users by checking their JSESSIONID
 *
 * @author Sergiy Gabriel
 */
public class AuthFilter implements Filter {

    private List<String> secureRoots;
    private AccountService accountService;
	private boolean isActive;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (isActive) {
			HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

			String target = httpRequest.getRequestURI();

			Optional<String> optional = this.secureRoots.stream().parallel()
					.filter(s -> target.matches(String.format("^%s%s[/]?$", httpRequest.getContextPath(), s)))
					.findFirst();

			if (!optional.isPresent()) {
				HttpSession session = httpRequest.getSession(false);
				if (session == null) {
					httpResponse.setStatus(SC_UNAUTHORIZED);
					return;
				}
				SessionUtils.getUserId(session);
			}
		}

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

    public void setSecureRoots(final List<String> secureRoots) {
        this.secureRoots = secureRoots == null ? emptyList() : unmodifiableList(secureRoots);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}