package dev.yoghurt1131.kledistemplate.core.accessor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.RedisConnectionFailureException
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

class KLedisTemplate<V> : RedisTemplate<String, V> {

    constructor(connectionFactory: RedisConnectionFactory, type: Class<V>) {
        super.setConnectionFactory(connectionFactory)
        keySerializer = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(type)
        val om = jacksonObjectMapper()
        jackson2JsonRedisSerializer.setObjectMapper(om)
        valueSerializer = jackson2JsonRedisSerializer
        hashKeySerializer = keySerializer
        hashValueSerializer = valueSerializer
        afterPropertiesSet()
    }

    /**
     * read value from Redis if the key exists.
     * return null if not exists, or catch some exceptions.
     */
    fun read(key: String): V? {
        try {
            val value: V? = this.opsForValue().get(key)
            if (value != null) {
                logger.info("Cache Hit.(key=$key)")
                return value
            }
            logger.info("Not in Cache.(key=$key)")
            return null
        } catch (exception: Exception) {
            logger.warn("Failed to get data from Redis. ${exception.message}")
            return null
        }
    }

    /**
     * write value into Redis.
     * No exceptions are thrown from this method.
     */
    fun write(key: String, value: V, timeout: Long, unit: TimeUnit) {
        try {
            opsForValue().set(key, value)
            expire(key, timeout, unit)
            logger.info("Store in Cache.(key=$key)")
        } catch (exception: RedisConnectionFailureException) {
            logger.warn("Failed to conect Redis. ${exception.message}")
        }
    }
}
