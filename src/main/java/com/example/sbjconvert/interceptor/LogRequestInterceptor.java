package com.example.sbjconvert.interceptor;

import com.example.sbjconvert.service.RequestLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.sbjconvert.common.Constants.REQUEST_END_TIME_ATTR;
import static com.example.sbjconvert.common.Constants.REQUEST_LOG_INTERCEPT_URI;

@AllArgsConstructor
@Slf4j
public class LogRequestInterceptor implements HandlerInterceptor {
    private final RequestLogService requestLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!request.getRequestURI().contains(REQUEST_LOG_INTERCEPT_URI)) {
            return;
        }
        request.setAttribute(REQUEST_END_TIME_ATTR, System.currentTimeMillis());
        requestLogService.log(request, response);
    }
}
