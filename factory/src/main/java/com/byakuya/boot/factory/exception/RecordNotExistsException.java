package com.byakuya.boot.factory.exception;

import com.byakuya.boot.factory.SystemVersion;

/**
 * Created by ganzl on 2020/11/30.
 */
public class RecordNotExistsException extends CustomizedException {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public RecordNotExistsException(String findKey) {
        super("ERR-20001", findKey);
    }
}
