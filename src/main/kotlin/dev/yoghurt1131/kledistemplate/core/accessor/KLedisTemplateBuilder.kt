package dev.yoghurt1131.kledistemplate.core.accessor

import org.springframework.data.redis.connection.RedisConnectionFactory

class KLedisTemplateBuilder(private val connectionFactory: RedisConnectionFactory) {

    /**
     * build KLedisTemplate of specified class
     */
    fun <T> build(type: Class<T>): KLedisTemplate<T> {
        return KLedisTemplate(connectionFactory, type)
    }
}
