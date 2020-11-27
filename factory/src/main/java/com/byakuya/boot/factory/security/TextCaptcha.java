package com.byakuya.boot.factory.security;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.exception.CustomizedException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2020/4/7.
 * 文本验证码实体
 */
public class TextCaptcha implements Serializable {

    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    /**
     * 文本验证码构造函数
     *
     * @param text  验证码
     * @param valid 有效期(单位秒)
     */
    private TextCaptcha(String text, long valid) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException();
        }
        this.text = StringUtils.trimWhitespace(text);
        this.localDateTime = LocalDateTime.now().plusSeconds(valid);
    }

    /**
     * 验证码是否失效
     *
     * @return 是否失效
     */
    private boolean isNotValid() {
        return LocalDateTime.now().isAfter(localDateTime);
    }

    public final String text;
    private final LocalDateTime localDateTime;

    public static TextCaptcha createCaptcha(HttpServletRequest request, String text, long validSecond) {
        TextCaptcha textCaptcha = new TextCaptcha(text, validSecond);
        request.getSession().setAttribute(ConstantUtils.SESSION_CAPTCHA_KEY, textCaptcha);
        return textCaptcha;
    }

    public static void verifyCaptcha(HttpServletRequest request) {
        String captcha = request.getParameter(ConstantUtils.CAPTCHA_PARAMETER_KEY);
        TextCaptcha textCaptcha = (TextCaptcha) request.getSession().getAttribute(ConstantUtils.SESSION_CAPTCHA_KEY);
        request.getSession().removeAttribute(ConstantUtils.SESSION_CAPTCHA_KEY);

        if (textCaptcha == null) {
            throw new CustomizedException("ERR-10001");
        }
        if (textCaptcha.isNotValid()) {
            throw new CustomizedException("ERR-10002");
        }
        if (!textCaptcha.isMatch(captcha)) {
            throw new CustomizedException("ERR-10003");
        }
    }

    /**
     * 验证码是否相等
     *
     * @param text 等待验证的字符串
     * @return 是否匹配
     */
    private boolean isMatch(String text) {
        return this.text.equalsIgnoreCase(StringUtils.trimWhitespace(text));
    }

    @Override
    public String toString() {
        return text;
    }
}
