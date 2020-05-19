# KLedis Template(Kotlin based Light weight Redis Template)

KLedisTemplate is a Light weight Redis accessor that extends Spring Data Redis's RedisTemplate, written in Kotlin.
KLedisTemplate let you easy to read, write String key JSON value format Redis Data.


# Usage

You can initialize KLedisTemplate through KLedisTempalteBuilder.

```kotlin
@Service
class ServiceImpl(
		// Autowired
        private val redisTemplateBuilder: KLedisTemplateBuilder,
) : Service {

	private val kledisTemplate = redisTemplateBuilder.build(Response::class.java)

	override fun doSomething(keyName: String): Response {
		// read redis value
		val cache = kledisTemplate.read(keyName)
		if (cache != null) {
			return buildResponse(cache)
		}

		// api call if cache missing
		val response: Response = apiWrapper.execute()

		// write redis value
		redisTemplate.write(keyName, response, 30, TimeUnit.MINUTES)
		return response
	}
}
```

# Interface

KLedisTemplate supports `read()` and `write()` methods.

```kotlin
class KLedisTemplate<V> : RedisTemplate<String, V> {

    fun read(key: String): V?

    fun write(key: String, value: V, timeout: Long, unit: TimeUnit)

}
```