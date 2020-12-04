package com.byakuya.boot.factory.page;

import com.byakuya.boot.factory.component.user.SecurityUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;

/**
 * Created by ganzl on 2020/11/27.
 */
@Controller
public class AuthenticationPageController {

    public AuthenticationPageController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@NotBlank @RequestParam(name = "id") String id
            , @NotBlank @RequestParam(name = "oldPassword") String oldPassword
            , @NotBlank @RequestParam(name = "newPassword") String newPassword) {
        return ResponseEntity.ok(securityUserService.changePassword(id, oldPassword, newPassword));
    }

    @GetMapping("/changePassword")
    public String changePasswordPageUrl() {
        return "changePassword";
    }

    @GetMapping("/")
    public String homePageUrl() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPageUrl() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPageUrl() {
        return "register";
    }

    private final SecurityUserService securityUserService;
}
