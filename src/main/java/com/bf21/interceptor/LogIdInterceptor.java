package com.bf21.interceptor;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@Component
public class LogIdInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogIdInterceptor.class);

    private static final Integer RANDOM_NUMERIC = 20;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String logId = RandomStringUtils.randomNumeric(RANDOM_NUMERIC);
            MDC.put("logId", logId);
            return true;
        } catch (Exception e) {
            printRequestDetails(request);
            LOGGER.error("Error on preHandle: ", e);
            throw new Exception("Error on preHandle: " + e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            MDC.remove("logId");
        } catch (Exception e) {
            printRequestDetails(request);
            LOGGER.error("Error on afterCompletion: ", e);
            throw new Exception("Error on afterCompletion: " + e.getMessage());
        }
    }

    private void printRequestDetails(ServletRequest request) {
        LOGGER.info("client_IP:" + request.getRemoteAddr());

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // Request URL
            LOGGER.info("Request: " + httpRequest.getRequestURL().toString());
            // Parameters
            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                LOGGER.info("Parameter " + entry.getKey() + "=" + Arrays.toString(entry.getValue()));
            }
            // Headers
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headers = httpRequest.getHeaders(headerName);
                while (headers.hasMoreElements()) {
                    String headerValue = headers.nextElement();
                    LOGGER.info("Header " + headerName + "=" + headerValue);
                }
            }
        }
    }

}