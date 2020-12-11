package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.DateSequenceTableGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2020/11/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_USER")
public class SecurityUser extends AbstractAuditableEntity<SecurityUser> {

    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    private LocalDateTime beginValidPeriod = LocalDateTime.now();
    @Embedded
    @Valid
    @NotNull
    private UserDetail detail;

    @Email
    @NotBlank
    @Column(unique = true, updatable = false, nullable = false)
    private String email;
    private LocalDateTime endValidPeriod;
    @Id
    @GeneratedValue(generator = "USER_ID_GENERATOR")
    @GenericGenerator(name = "USER_ID_GENERATOR"
            , strategy = "com.byakuya.boot.factory.component.DateSequenceTableGenerator"
            , parameters = {
            @Parameter(name = DateSequenceTableGenerator.PK_PREFIX_PARAM, value = "USER")
    })
    private String id;
    private LocalDateTime lastPasswordModifiedDate;
    private boolean locked;
    @NotBlank
    private String password;
    @NotBlank
    @Pattern(regexp = "^1[0-9]{10}$")
    @Column(unique = true, updatable = false, nullable = false)
    private String phone;
    @NotBlank
    @Column(unique = true, updatable = false, nullable = false)
    private String username;
}
