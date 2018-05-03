package com.hitales.markable.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, HEAD, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-TOTAL-COUNT");
        response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, X-Auth-Token, Content-Type, X-TOTAL-COUNT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Vary","Origin");
//        if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
////            chain.doFilter(req, res);
////        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}