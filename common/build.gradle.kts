import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


//sourceSets {
//    val samples by creating {
//        compileClasspath += sourceSets["main"].output
//        runtimeClasspath += sourceSets["main"].output
//    }
//}
//
//configurations {
//    val samplesImplementation by getting {
//        extendsFrom(configurations["implementation"])
//    }
//}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(Dependencies.`kotlinx-datetime`)
                implementation("com.ionspin.kotlin:bignum:0.3.1") 
                implementation("io.ktor:ktor-io:1.5.4")
            }
        }
    }
}
