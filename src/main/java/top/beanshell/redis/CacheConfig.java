package top.beanshell.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import top.beanshell.redis.properties.RedisProperties;
import top.beanshell.redis.serializer.CustomStringSerializer;

import javax.annotation.Resource;

/**
 * 缓存配置
 * @author binchao
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "ram.application.redis.enable", havingValue = "true")
public class CacheConfig extends CachingConfigurerSupport {

    @Resource
    private RedisProperties redisProperties;

    @Resource
    private CustomStringSerializer customStringSerializer;

    @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    /**
     * RedisTemplate with prefix for Object
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Integer
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Integer> redisIntegerTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Long
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Long> redisLongTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for String
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisStringTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Boolean
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Boolean> redisBooleanTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Double
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Double> redisDoubleTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Float
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Float> redisFloatTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Short
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Short> redisShortTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate with prefix for Byte
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Byte> redisByteTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        template.setKeySerializer(customStringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * default key format with prefix
     */
    private static final String KEY_FORMAT = "%s:%s";

    /**
     * redis cache config
     * @return
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                // 为Cacheable等注解增加前缀
                .computePrefixWith(cacheName -> {
                    if (StringUtils.hasText(redisProperties.getKeyPrefix())) {
                        return String.format(KEY_FORMAT, redisProperties.getKeyPrefix(), cacheName);
                    } else {
                        return cacheName;
                    }
                })
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer()));
    }

    /**
     * cache manager
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration());

    }
}
