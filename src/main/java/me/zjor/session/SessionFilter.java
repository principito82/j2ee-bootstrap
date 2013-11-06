package me.zjor.session;

import lombok.extern.slf4j.Slf4j;
import me.zjor.util.CookieUtils;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Sergey Royz
 * @since: 04.11.2013
 */
@Slf4j
public class SessionFilter implements Filter {

    public static final String SESSION_ID_COOKIE_KEY = "ssid";

    @Inject
    private Session session;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String sessionId = CookieUtils.getCookieValue(request, SESSION_ID_COOKIE_KEY);

        if (sessionId == null || session.getSession(sessionId) == null) {
            log.info("Session doesn't exist. Creating...");
            sessionId = session.createSession();
            CookieUtils.setCookie(response, SESSION_ID_COOKIE_KEY, sessionId);
        }

        Session.setCurrent(session.getSession(sessionId));

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}