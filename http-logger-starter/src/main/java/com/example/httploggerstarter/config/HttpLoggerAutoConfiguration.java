package com.example.httploggerstarter.config;

import com.example.httploggerstarter.filter.HttpLoggerFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Автоконфигурация для настройки {@link HttpLoggerFilter}.
 * <p>
 * Этот класс автоматически настраивает {@link HttpLoggerFilter} в случае, если он отсутствует в контексте приложения и если
 * свойство {@code request-logger.enabled} установлено в {@code true} (по умолчанию).
 * <p>
 * Если {@link HttpLoggerFilter} уже определен в контексте приложения, то этот бин не будет создан.
 * </p>
 *
 * @see HttpLoggerFilter
 * @see HttpLoggerProperties
 */
@AutoConfiguration
@EnableConfigurationProperties(HttpLoggerProperties.class)
@ConditionalOnProperty(prefix = "request-logger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HttpLoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpLoggerFilter filter() {
        return new HttpLoggerFilter();
    }
}
