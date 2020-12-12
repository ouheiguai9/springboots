package com.byakuya.boot.factory.exception;

import com.byakuya.boot.factory.SystemVersion;

/**
 * Created by ganzl on 2020/12/12.
 */
public class UnsupportedOperationException extends CustomizedException {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public UnsupportedOperationException() {
        super("ERR-10004");
    }
}
