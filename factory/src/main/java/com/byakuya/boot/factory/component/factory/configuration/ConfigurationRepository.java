package com.byakuya.boot.factory.component.factory.configuration;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Created by ganzl on 2021/4/26.
 */
public interface ConfigurationRepository extends PagingAndSortingRepository<Configuration, String> {
    Optional<Configuration> findByCreatedBy_idAndConfigurationType(String userId, Configuration.ConfigurationType configurationType);
}
