package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.SystemVersion;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by ganzl on 2020/11/30.
 */
@Data
@Embeddable
public class UserDetail implements Serializable {
    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    @NotBlank
    private String nickname;
}
