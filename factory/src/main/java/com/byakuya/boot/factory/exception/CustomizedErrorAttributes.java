package com.byakuya.boot.factory.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ganzl on 2020/6/11.
 * 自定义错误属性(增加对CustomizedException处理以及增加code属性)
 */
@Component
public class CustomizedErrorAttributes extends DefaultErrorAttributes {

    public CustomizedErrorAttributes(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof CustomizedException) {
            CustomizedException cus = (CustomizedException) ex;
            if (!StringUtils.hasText(cus.getExceptionMessage())) {
                cus.setExceptionMessage(messageSource.getMessage(cus.getExceptionCode(), cus.getArgs(), Locale.getDefault()));
            }
        }
        return super.resolveException(request, response, handler, ex);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> rtnVal = super.getErrorAttributes(webRequest, options);
        Throwable throwable = getError(webRequest);
        if (throwable instanceof CustomizedException) {
            rtnVal.put("code", ((CustomizedException) throwable).getExceptionCode());
        }
        return rtnVal;
    }

    private final MessageSource messageSource;
}
