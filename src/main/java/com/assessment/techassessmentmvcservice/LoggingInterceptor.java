package com.assessment.techassessmentmvcservice;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        logger.info("Incoming request: {} {} {}", request.getMethod(),
                request.getRequestURI(), requestWrapper.getContentAsString());
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) {
        logger.info("Outgoing response: {}", response.getStatus());
    }
}

