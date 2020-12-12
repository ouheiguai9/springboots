package com.byakuya.boot.factory.page;

import com.byakuya.boot.factory.component.user.SecurityUser;
import com.byakuya.boot.factory.component.user.SecurityUserService;
import com.byakuya.boot.factory.property.CaptchaProperties;
import com.byakuya.boot.factory.property.SecurityProperties;
import com.byakuya.boot.factory.security.AuthenticationUser;
import com.byakuya.boot.factory.security.TextCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2020/11/27.
 */
@Controller
@Validated
public class AuthenticationPageController {

    public AuthenticationPageController(SecurityUserService securityUserService, SecurityProperties securityProperties) {
        this.securityUserService = securityUserService;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@NotBlank String username
            , @NotBlank String oldPassword
            , @NotBlank String newPassword) {
        return ResponseEntity.ok(securityUserService.changePassword(username, oldPassword, newPassword));
    }

    @GetMapping("/changePassword")
    public String changePasswordPageUrl(@AuthenticationPrincipal AuthenticationUser user
            , @RequestParam(name = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, required = false) String username
            , @SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception
            , HttpServletRequest request
            , Model model) {
        if (user != null) {
            username = user.getUsername();
        }
        if (!StringUtils.hasText(username)) {
            return "redirect:/login";
        }
        if (exception != null) {
            model.addAttribute("error", exception.getMessage());
            request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        model.addAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        return "changePassword";
    }

    @GetMapping("/")
    public String homePageUrl() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPageUrl(@AuthenticationPrincipal AuthenticationUser user
            , @SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception
            , HttpServletRequest request
            , Model model) {
        if (user != null) return "redirect:/logout";
        if (exception != null) {
            model.addAttribute("error", exception.getMessage());
            request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        model.addAttribute("allowRegistration", securityProperties.isAllowRegistration());
        model.addAttribute("allowCaptcha", allowCaptcha());
        return "login";
    }

    private boolean allowCaptcha() {
        return captchaProperties != null && captchaProperties.isEnable();
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<SecurityUser> register(@Valid @RequestBody SecurityUser securityUser
            , HttpServletRequest request) {
        if (!securityProperties.isAllowRegistration()) throw new UnsupportedOperationException();
        if (allowCaptcha()) {
            TextCaptcha.verifyCaptcha(request);
        }
        securityUser.setLastPasswordModifiedDate(LocalDateTime.now());
        return ResponseEntity.ok(securityUserService.regist(securityUser));
    }

    @GetMapping("/register")
    public String registerPageUrl(Model model) {
        model.addAttribute("allowCaptcha", allowCaptcha());
        return securityProperties.isAllowRegistration() ? "register" : "redirect:/login";
    }

    @Autowired(required = false)
    public void setCaptchaProperties(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    private final SecurityProperties securityProperties;
    private final SecurityUserService securityUserService;
    private CaptchaProperties captchaProperties;
}
