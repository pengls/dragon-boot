package com.dragon.boot.web.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName XssFilterBaseWrapper
 * @Author pengl
 * @Date 2019-10-16 8:38
 * @Description 基于Request包装器的xss过滤器
 * @Version 1.0
 */
public class XssFilterBaseWrapper implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(xssHttpServletRequestWrapper, servletResponse);
    }
}
