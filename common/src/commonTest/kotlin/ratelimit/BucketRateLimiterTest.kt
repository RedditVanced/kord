package ratelimit

import dev.kord.common.ratelimit.BucketRateLimiter
import fixed
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import runBlockingTest
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.asserter
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class BucketRateLimiterTest {

    val interval = Duration.milliseconds(1_000_000)
    val instant = Clock.System.now()
    val clock = Clock.fixed(instant)
    lateinit var rateLimiter: BucketRateLimiter

    @BeforeTest
    fun setup() {
        rateLimiter = BucketRateLimiter(1, interval, clock)
    }

    @JsName("underCapacity")
    @Test
    fun `a bucket rate limiter does not ratelimit when under capacity`() = runBlockingTest {
        rateLimiter.consume()

        asserter.assertTrue("expected timeout of 0 ms but was $currentTime ms", 0L == currentTime)
    }

    @JsName("overCapacity")
    @Test
    fun `a bucket rate limiter does ratelimit when over capacity`() = runBlockingTest {
        rateLimiter.consume()
        rateLimiter.consume()

        asserter.assertTrue(
            "expected timeout of ${interval.inWholeMilliseconds} ms but was $currentTime ms",
            interval.inWholeMilliseconds == currentTime
        )
    }

}
