package com.byakuya.boot.factory.component;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Created by ganzl on 2020/11/18.
 */
@Component
public class DateSequenceTableGenerator extends TableGenerator {
    public static final String PK_PREFIX_PARAM = "segment_prefix_value";

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = ConfigurationHelper.getString(PK_PREFIX_PARAM, params, ConfigurationHelper.getString(IdentifierGenerator.JPA_ENTITY_NAME, params));
        segmentPrefixValue = String.format("%s-%s", prefix, dateStr);
        params.replace(TableGenerator.SEGMENT_VALUE_PARAM, segmentPrefixValue);
        super.configure(type, params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable id = super.generate(session, obj);
        return String.format("%s-%05d", segmentPrefixValue, Long.parseLong(id.toString()));
    }

    private String segmentPrefixValue;
}
