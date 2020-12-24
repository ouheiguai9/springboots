package com.byakuya.boot.factory.exception;

import com.byakuya.boot.factory.ConstantUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
        String errorAttributeStr = webRequest.getHeader(ConstantUtils.HEADER_ERROR_ATTRIBUTE_KEY);
        ErrorAttributeOptions newOptions = options.including(Include.EXCEPTION, Include.MESSAGE);
        if (!StringUtils.hasText(errorAttributeStr) || !errorAttributeStr.contains(Include.STACK_TRACE.toString())) {
            newOptions = newOptions.excluding(Include.STACK_TRACE, Include.BINDING_ERRORS);
        }
        Map<String, Object> rtnVal = super.getErrorAttributes(webRequest, newOptions);
        Throwable throwable = getError(webRequest);
        if (throwable instanceof CustomizedException) {
            rtnVal.put("code", ((CustomizedException) throwable).getExceptionCode());
        } else if(throwable instanceof BindException) {
            List<ObjectError> errors = ((BindException) throwable).getAllErrors();
            if (errors != null && !errors.isEmpty()) {
                rtnVal.put("message", errors.stream().map(error -> {
                    String property = error.getObjectName();
                    if (error instanceof FieldError) {
                        property = ((FieldError) error).getField();
                    }
                    return String.format("%s: %s", property, error.getDefaultMessage());
                }).collect(Collectors.joining("\r\n")));
            }
            rtnVal.remove("errors");
        }
        return rtnVal;
    }

    private final MessageSource messageSource;
}
