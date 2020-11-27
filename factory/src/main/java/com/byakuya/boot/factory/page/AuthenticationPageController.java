package com.byakuya.boot.factory.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ganzl on 2020/11/27.
 */
@Controller
public class AuthenticationPageController {

    @GetMapping("/")
    public String homePageUrl() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPageUrl() {
        return "login";
    }
}
