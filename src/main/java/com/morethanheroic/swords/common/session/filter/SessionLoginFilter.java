package com.morethanheroic.swords.common.session.filter;

import com.morethanheroic.swords.common.session.SessionAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionLoginFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SessionLoginFilter.class);

    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.info("Initialising SessionCheckerFilter");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getSession().getAttribute(SessionAttributeType.USER_ID.name()) == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());

            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        logger.info("Destroying SessionCheckerFilter");
    }
}