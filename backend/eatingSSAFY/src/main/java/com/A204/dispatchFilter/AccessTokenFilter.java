package com.A204.dispatchFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenFilter implements Filter {
    private static final Set<UrlObject> apiSet = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        apiSet.add(new UrlObject("/api/preference/list", "GET"));
        apiSet.add(new UrlObject("/api/preference", "POST"));
        apiSet.add(new UrlObject("/api/daily-visit", "GET"));
        apiSet.add(new UrlObject("/api/daily-visit", "POST"));
        apiSet.add(new UrlObject("/api/user", "GET"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // cookie 가져오기
        Cookie[] cookies = request.getCookies();
        Object user = null;
        if (cookies != null && cookies.length != 0) {
            Optional<Cookie> cookie = Arrays.stream(cookies).filter(o -> o.getName().equals("userId")).findFirst();
            if (cookie.isPresent())
                user = cookie.get().getValue();
        }

        // 필요한 변수
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean userExist = user != null;
        String path = request.getServletPath();
        String method = request.getMethod();

        if (userExist) {
            AddParamHttpRequest modifiedRequest = new AddParamHttpRequest(request);
            modifiedRequest.setParameter("userId", user.toString());
            request = (HttpServletRequest) modifiedRequest;
        }
        // api url 구분
        if (apiSet.contains(new UrlObject(path, method)) && !userExist) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Data
    @AllArgsConstructor
    static class UrlObject {
        String url;
        String method;
    }
}