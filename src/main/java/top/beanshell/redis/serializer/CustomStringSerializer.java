package top.beanshell.redis.serializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.beanshell.redis.properties.RedisProperties;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * redis custom serializer
 * @author binchao
 */
@Component
@Slf4j
public class CustomStringSerializer implements RedisSerializer<String> {

    @Resource
    private RedisProperties redisProperties;

    private final Charset charset;

    /**
     * default key format with prefix
     */
    private static final String KEY_FORMAT = "%s:%s";


    public CustomStringSerializer() {
        this ( StandardCharsets.UTF_8 );
    }

    public CustomStringSerializer(Charset charset) {
        Assert.notNull ( charset, "Charset must not be null!" );
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        String keyPrefix = redisProperties.getKeyPrefix();
        String saveKey = new String(bytes,charset);
        int indexOf = saveKey.indexOf(keyPrefix);
        if (indexOf > 0) {
            log.warn("key missing prefix");
        } else {
            saveKey = saveKey.substring(indexOf);
        }
        return (saveKey.getBytes () == null ? null : saveKey);
    }

    @Override
    public byte[] serialize(String keyName) {
        String key = null;
        if (StringUtils.hasText(redisProperties.getKeyPrefix())) {
            key = String.format(KEY_FORMAT, redisProperties.getKeyPrefix(), keyName);
        }
        return (key == null ? null : key.getBytes(charset));
    }
}
