package com.dt.user.exception;

import com.dt.user.config.BaseApiService;
import com.dt.user.utils.HttpServletUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class DBExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 没有权限抛出异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        return BaseApiService.setResultError("没有权限操作");
    }
}
