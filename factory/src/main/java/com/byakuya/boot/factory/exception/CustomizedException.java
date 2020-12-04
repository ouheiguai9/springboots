package com.byakuya.boot.factory.exception;

import com.byakuya.boot.factory.SystemVersion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Created by ganzl on 2020/11/18.
 */
public class CustomizedException extends RuntimeException {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public CustomizedException(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public CustomizedException(String exceptionCode, String... args) {
        this.exceptionCode = exceptionCode;
        this.args = args;
    }

    public CustomizedException(Throwable cause, String exceptionCode, String... args) {
        super(cause);
        this.exceptionCode = exceptionCode;
        this.args = args;
    }

    public CustomizedException(Throwable cause, String exceptionCode) {
        super(cause);
        this.exceptionCode = exceptionCode;
    }

//    public CustomizedException(String exceptionCode, String exceptionMessage) {
//        this.exceptionCode = exceptionCode;
//        this.exceptionMessage = exceptionMessage;
//    }
//
//    public CustomizedException(Throwable cause, String exceptionCode, String exceptionMessage) {
//        super(cause);
//        this.exceptionCode = exceptionCode;
//        this.exceptionMessage = exceptionMessage;
//    }

    public String[] getArgs() {
        return args == null ? args : args.clone();
    }

    @Override
    public String getMessage() {
        return StringUtils.hasText(exceptionMessage) ? exceptionMessage : super.getMessage();
    }

    private String[] args;
    @Getter
    private String exceptionCode;
    @Setter
    @Getter
    private String exceptionMessage;
}
