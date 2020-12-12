package com.byakuya.boot.factory.page;

import com.byakuya.boot.factory.component.user.SecurityUser;
import com.byakuya.boot.factory.component.user.SecurityUserService;
import com.byakuya.boot.factory.security.AuthenticationUser;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * Created by ganzl on 2020/11/27.
 */
@Controller
@Validated
public class AuthenticationPageController {

    public AuthenticationPageController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
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
            , HttpServletResponse response
            , Model model) throws IOException {
        if (user != null) {
            username = user.getUsername();
        }
        if (exception != null) {
            model.addAttribute("error", exception.getMessage());
            request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        if (!StringUtils.hasText(username)) {
            response.sendRedirect(loginPageUrl(null, null, null));
        }
        model.addAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        return "changePassword";
    }

    @GetMapping("/login")
    public String loginPageUrl(@SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception
            , HttpServletRequest request
            , Model model) {
        if (exception != null) {
            model.addAttribute("error", exception.getMessage());
            request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        return "login";
    }

    @GetMapping("/")
    public String homePageUrl() {
        return "home";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<SecurityUser> register(@Valid @RequestBody SecurityUser securityUser) {
        return ResponseEntity.ok(securityUserService.regist(securityUser));
    }

    @GetMapping("/register")
    public String registerPageUrl() {
        return "register";
    }

    private final SecurityUserService securityUserService;
}
