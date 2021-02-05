package com.byakuya.boot.factory.config;

import com.byakuya.boot.factory.ConstantUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ganzl on 2021/2/4.
 */
@ControllerAdvice
public class GlobalAdvice {
    @ModelAttribute
    public void addTokenAttributes(HttpServletRequest request, Model model) {
        Object token = request.getAttribute(ConstantUtils.AUTH_PAGE_TOKEN_KEY);
        if (token != null) {
            model.addAttribute(ConstantUtils.AUTH_PAGE_TOKEN_KEY, token.toString());
            request.removeAttribute(ConstantUtils.AUTH_PAGE_TOKEN_KEY);
        }
    }
}
