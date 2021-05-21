import kotlinx.coroutines.test.TestCoroutineScope

actual fun <T> runBlockingTest(block: suspend TestCoroutineScope.() -> T): Any =
    kotlinx.coroutines.test.runBlockingTest { block() }

actual typealias TestCoroutineScope = TestCoroutineScope
