package com.byakuya.boot.factory.component.factory.configuration;

import com.byakuya.boot.factory.SystemVersion;
import com.byakuya.boot.factory.component.AbstractAuditableEntity;
import com.byakuya.boot.factory.component.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by ganzl on 2021/4/26.
 */
@Setter
@Getter
@Entity
@Table(name = "T_APP_FACTORY_CONFIGURATION")
public class Configuration extends AbstractAuditableEntity<User> {

    private static final long serialVersionUID = SystemVersion.SERIAL_VERSION_UID;
    private ConfigurationType configurationType;
    @Column(length = 1000)
    private String content;
    @Id
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @GeneratedValue(generator = "system_uuid")
    private String id;

    public enum ConfigurationType {
        EfficientRank("效率排行");

        ConfigurationType(String name) {
            this.name = name;
        }

        @Getter
        private String name;
    }
}
