package top.beanshell.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis config property
 * @author binchao
 */
@Component
@ConfigurationProperties(prefix = "ram.application.redis")
@Data
public class RedisProperties {

    /**
     * whether to enable
     */
    private Boolean enable;

    /**
     * redis host
     */
    private String host;

    /**
     * redis connect port
     */
    private Integer port;

    /**
     * redis password
     */
    private String password;

    /**
     * redis database
     */
    private Integer database;

    /**
     * redis key prefix
     */
    private String keyPrefix;
}
