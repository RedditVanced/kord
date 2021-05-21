import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
actual fun <T> runBlockingTest(block: suspend TestCoroutineScope.() -> T): dynamic {
    val scope = TimeMarkTestCoroutineScope()
    return GlobalScope.promise(TestDispatcher(scope)) {
        block(scope)
    }
}

actual interface TestCoroutineScope {
    actual val currentTime: Long
}

private class TimeMarkTestCoroutineScope(override var currentTime: Long = 0) : TestCoroutineScope

@OptIn(InternalCoroutinesApi::class)
private class TestDispatcher(private val scope: TimeMarkTestCoroutineScope) : CoroutineDispatcher(), Delay {
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        scope.currentTime += timeMillis
        continuation.resume(Unit) {}
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Dispatchers.Default.dispatch(context + this, block)  // dispatch on CommonPool
    }
}
