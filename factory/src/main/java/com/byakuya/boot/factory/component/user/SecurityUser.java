package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.DateSequenceTableGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2020/11/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_USER")
public class SecurityUser extends AbstractAuditableEntity<SecurityUser> {

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    private LocalDateTime beginValidPeriod;
    @Embedded
    private UserDetail detail;
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
    private String password;
    private String phone;
    private String username;
}
