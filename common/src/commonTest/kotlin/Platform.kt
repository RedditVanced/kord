expect fun <T> runBlockingTest(block: suspend TestCoroutineScope.() -> T): Any

expect interface TestCoroutineScope {
    val currentTime: Long
}
