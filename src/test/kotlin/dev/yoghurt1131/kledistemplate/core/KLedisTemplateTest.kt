package dev.yoghurt1131.kledistemplate.core

import dev.yoghurt1131.kledistemplate.core.accessor.KLedisTemplate
import io.mockk.*
import org.mockito.MockitoAnnotations
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.springframework.data.redis.RedisConnectionFailureException
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ValueOperations
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNull


object CustomRedisTemplateTest: Spek( {

    // object setup
    val connectionFactory by memoized {  mockk<RedisConnectionFactory>() }
    val valueOperations by memoized { mockk<ValueOperations<String, KLedisValue>>() }
    val type by memoized { KLedisValue::class.java }
    val target by memoized { spyk(KLedisTemplate(connectionFactory, type)) }

    // data setup
    val key by memoized { "kledis" }

    val value by memoized { KLedisValue(100, "Second") }

    describe(".read()") {
        beforeEachTest {
            MockitoAnnotations.initMocks(this)
        }

        it("returns value if present") {
            every { valueOperations.get(key) } returns value
            every { target.opsForValue() } returns valueOperations

            val actual = target.read(key)

            assertEquals(value, actual);
        }

        it("returns null when value is not in Cache.") {
            every { valueOperations.get(key) } returns null
            every { target.opsForValue() } returns valueOperations

            val actual = target.read(key)

            assertNull(actual)
        }

        it("returns null when exception has occurred") {
            every { valueOperations.get(key) } throws RuntimeException("DUMMY")
            every { target.opsForValue() } returns valueOperations

            val actual = target.read(key)

            assertNull(actual)
        }
    }

    describe(".write()") {
        beforeEachTest {
            MockitoAnnotations.initMocks(this)
        }

        it("stores key and value.") {
            every { valueOperations.set(key, value) } just Runs
            every { target.expire(key, 1000, TimeUnit.MILLISECONDS) } returns true
            every { target.opsForValue() } returns valueOperations

            target.write(key, value, 1000, TimeUnit.MILLISECONDS)
        }

        it("do nothing when exception occurred.") {
            every { valueOperations.set(key, value) } throws RedisConnectionFailureException("DUMMY")
            every { target.expire(key, 1000, TimeUnit.MILLISECONDS) } returns true
            every { target.opsForValue() } returns valueOperations

            target.write(key, value, 1000, TimeUnit.MILLISECONDS)
        }

    }

})
data class KLedisValue(val firstValue: Int, val secondValue: String)


