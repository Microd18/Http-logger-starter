package com.example.httploggerstarter.config;

import com.example.httploggerstarter.filter.HttpLoggerFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Свойства конфигурации для настройки логирования HTTP запросов.
 * <p>
 * Этот класс предоставляет доступ к свойствам, определенным в конфигурационном файле приложения, для настройки
 * {@link HttpLoggerFilter}. Свойства можно задать с помощью префикса {@code config}.
 * </p>
 *
 * @see HttpLoggerFilter
 */
@Data
@ConfigurationProperties(prefix = "config")
public class HttpLoggerProperties {

    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
