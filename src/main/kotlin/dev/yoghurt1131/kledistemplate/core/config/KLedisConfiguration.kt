package dev.yoghurt1131.kledistemplate.core.config

import dev.yoghurt1131.kledistemplate.core.accessor.KLedisTemplateBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name =["kledis.enable"], matchIfMissing = true)
@EnableConfigurationProperties
class KLedisConfiguration {

    @Bean
    fun redisTemplateBuilder(connectionFactory: RedisConnectionFactory): KLedisTemplateBuilder {
        return KLedisTemplateBuilder(connectionFactory)
    }

}