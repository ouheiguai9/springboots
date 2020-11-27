package com.byakuya.boot.factory.component.user;

import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.DateSequenceTableGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by ganzl on 2020/11/18.
 */
@Setter
@Getter
@Entity
@Table(name = "T_SYS_USER")
public class SecurityUser extends AbstractAuditableEntity<SecurityUser> {
    private LocalDateTime beginValidPeriod;
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
