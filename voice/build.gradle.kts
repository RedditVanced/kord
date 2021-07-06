dependencies {

    api(common)
    api(gateway)

    api("com.sedmelluq:lavaplayer:1.3.77") // will remove once i get this working
    api(Dependencies.`ktor-client-json`)
    api(Dependencies.`ktor-client-json-jvm`)
    api(Dependencies.`ktor-client-websocket`)
    api(Dependencies.`ktor-client-serialization-jvm`)
    api(Dependencies.`ktor-client-cio`)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = Jvm.target
        freeCompilerArgs = listOf(
            CompilerArguments.coroutines,
            CompilerArguments.time,
            CompilerArguments.stdLib,
            CompilerArguments.optIn
        )
    }
}
