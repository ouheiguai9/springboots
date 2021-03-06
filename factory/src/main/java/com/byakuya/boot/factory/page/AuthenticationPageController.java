package com.byakuya.boot.factory.page;

import com.byakuya.boot.factory.component.menu.Menu;
import com.byakuya.boot.factory.component.menu.MenuRepository;
import com.byakuya.boot.factory.component.user.User;
import com.byakuya.boot.factory.component.user.UserService;
import com.byakuya.boot.factory.config.property.CaptchaProperties;
import com.byakuya.boot.factory.config.property.SecurityProperties;
import com.byakuya.boot.factory.security.AuthenticationUser;
import com.byakuya.boot.factory.security.CustomizedGrantedAuthority;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ganzl on 2020/11/27.
 */
@Controller
@Validated
public class AuthenticationPageController {

    public AuthenticationPageController(MenuRepository menuRepository, UserService userService, SecurityProperties securityProperties) {
        this.menuRepository = menuRepository;
        this.userService = userService;
        this.securityProperties = securityProperties;
    }

    @GetMapping("/auth/page/{module}/{component}")
    public String anyDynamicAuthPageUrl(@PathVariable String module, @PathVariable String component) {
        return String.format("module/%s/%s", module, component);
    }

    @PostMapping("/changePassword")
    @ResponseBody
    public ResponseEntity<Boolean> changePassword(@NotBlank String username
            , @NotBlank String oldPassword
            , @NotBlank String newPassword) {
        return ResponseEntity.ok(userService.changePassword(username, oldPassword, newPassword));
    }

    @GetMapping("/changePassword")
    public String changePasswordPageUrl(@RequestParam(name = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, required = false) String username
            , @SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception
            , HttpServletRequest request
            , Model model) {
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

    @GetMapping("/home")
    public String defaultHomePageUrl() {
        return "home";
    }

    @GetMapping("/authMenus")
    @ResponseBody
    public ResponseEntity<List<Menu>> getAuthMenuList(@AuthenticationPrincipal AuthenticationUser user) {
        return ResponseEntity.ok(getAuthMenus(user.getAuthority()));
    }

    /**
     * 获取已经授权的菜单
     *
     * @param authority 权限
     * @return 已经授权的菜单
     */
    private List<Menu> getAuthMenus(CustomizedGrantedAuthority authority) {
        List<Menu> menuList = menuRepository.findAll().stream().filter(x -> x.noParent() && authority.check(x.getCode())).collect(Collectors.toList());
        menuList.forEach(menu -> menu.setOrderChildren(menu.getOrderChildren().stream().filter(x -> authority.check(menu.getCode(), x.getCode())).collect(Collectors.toList())));
        return menuList;
    }

    @GetMapping("/")
    public String homePageUrl(@AuthenticationPrincipal AuthenticationUser user
            , Model model) {
        List<Menu> topMenuList = getAuthMenus(user.getAuthority());
        if (topMenuList.size() == 1) {
            topMenuList = new ArrayList<>(topMenuList.get(0).getOrderChildren());
        }
        String nickname = securityProperties.getAdmin().getNickname(), username = user.getUsername(), avatar = "";
        if (!user.isAdmin()) {
            User me = userService.get(user.getUserId());
            nickname = me.getNickname();
            avatar = me.getAvatar();
        }
        model.addAttribute("topMenuList", topMenuList);
        model.addAttribute("nickname", nickname);
        model.addAttribute("username", username);
        if (StringUtils.hasText(avatar)) {
            model.addAttribute("avatar", avatar);
        }
        return "index";
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

    @PutMapping("/personalDetail")
    public ResponseEntity<User> modify(@RequestBody User user) {
        return ResponseEntity.ok(userService.modifyPart(user));
    }

    @GetMapping("/personalDetail")
    public String personalDetailPageUrl(@AuthenticationPrincipal AuthenticationUser user, Model model) {
        if (user.isAdmin()) return "404";
        model.addAttribute("me", userService.get(user.getUserId()));
        return "personalDetail";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<User> register(@Valid @RequestBody User user
            , HttpServletRequest request) {
        if (!securityProperties.isAllowRegistration()) throw new UnsupportedOperationException();
        if (allowCaptcha()) {
            TextCaptcha.verifyCaptcha(request);
        }
        user.setLastPasswordModifiedDate(LocalDateTime.now());
        return ResponseEntity.ok(userService.regist(user));
    }

    @GetMapping("/register")
    public String registerPageUrl(Model model) {
        model.addAttribute("allowCaptcha", allowCaptcha());
        return securityProperties.isAllowRegistration() ? "register" : "redirect:/login";
    }

    @GetMapping("/safeChangePassword")
    public String safeChangePasswordPageUrl(@AuthenticationPrincipal AuthenticationUser user, Model model) {
        if (user.isAdmin()) return "404";
        model.addAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername());
        return "changePassword";
    }

    @Autowired(required = false)
    public void setCaptchaProperties(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    private final MenuRepository menuRepository;
    private final SecurityProperties securityProperties;
    private final UserService userService;
    private CaptchaProperties captchaProperties;
}
