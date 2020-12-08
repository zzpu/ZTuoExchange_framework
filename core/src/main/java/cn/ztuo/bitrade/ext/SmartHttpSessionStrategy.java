package cn.ztuo.bitrade.ext;

import org.apache.commons.lang.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class SmartHttpSessionStrategy implements HttpSessionIdResolver {
    private CookieHttpSessionIdResolver cookie;
    private HeaderHttpSessionIdResolver xauth;
    private String tokenName = "x-auth-token";

    public SmartHttpSessionStrategy(CookieHttpSessionIdResolver cookie, HeaderHttpSessionIdResolver xauth) {
        this.cookie = cookie;
        this.xauth = xauth;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        if (isXAuth(request)) {
            return xauth.resolveSessionIds(request);
        }
        return cookie.resolveSessionIds(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        if (isXAuth(request)) {
            xauth.setSessionId(request, response, sessionId);
        } else {
            cookie.setSessionId(request, response, sessionId);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        if (isXAuth(request)) {
            xauth.expireSession(request, response);
        } else {
            cookie.expireSession(request, response);
        }
    }

    private boolean isXAuth(HttpServletRequest request) {
        return request.getHeader(tokenName) != null;
    }
}