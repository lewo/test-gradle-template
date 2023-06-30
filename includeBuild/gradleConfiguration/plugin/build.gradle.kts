plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    google()
    mavenCentral()
    jcenter() // Warning: this repository is going to shut down soon
    gradlePluginPortal()
}

gradlePlugin {
    plugins.register("android-lib-plugin") {
        id = "android-lib-plugin"
        implementationClass = "com.example.android.gradle.configuration.app.AndroidLibPlugin"
    }

    plugins.register("app-plugin") {
        id = "app-plugin"
        implementationClass = "com.example.android.gradle.configuration.app.AppPlugin"
    }

    plugins.register("hilt-plugin") {
        id = "hilt-plugin"
        implementationClass = "com.example.android.gradle.configuration.hilt.HiltPlugin"
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest()
        }

        // Create a new test suite
        val functionalTest by registering(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest()

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project)
            }

            targets {
                all {
                    // This test suite should run after the built-in test suite has run its tests
                    testTask.configure { shouldRunAfter(test) }
                }
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

gradlePlugin.testSourceSets(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}

dependencies {
    implementation(libs.gradle.build.tool)
    implementation(libs.kotlin.gradle.plugin)
}
