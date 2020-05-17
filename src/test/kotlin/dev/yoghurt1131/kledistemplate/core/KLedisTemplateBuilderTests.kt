package dev.yoghurt1131.kledistemplate.core

import dev.yoghurt1131.kledistemplate.core.accessor.KLedisTemplate
import dev.yoghurt1131.kledistemplate.core.accessor.KLedisTemplateBuilder
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.data.redis.connection.RedisConnectionFactory
import kotlin.test.assertEquals

object KLedisTemplateBuilderTests : Spek({

    val connectionFactory by memoized { mockk<RedisConnectionFactory>() }

    describe("build()") {
        it ("returns CustomizedRedisTemplate using connectionFactory and Expected Type") {
            val redisTemplateBuilder = KLedisTemplateBuilder(connectionFactory)
            val actual = redisTemplateBuilder.build(String::class.java)
            assertEquals(KLedisTemplate<String>::javaClass.name, actual::javaClass.name)
        }
    }
})