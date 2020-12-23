package com.byakuya.boot.factory.exception;

import com.byakuya.boot.factory.SystemVersion;

/**
 * Created by ganzl on 2020/12/23.
 */
public class FileIOException extends CustomizedException {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    public FileIOException() {
        this(null);
    }

    public FileIOException(Throwable cause) {
        super(cause, "ERR-10005");
    }
}
