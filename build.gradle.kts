// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // bug at https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.library) apply false
    alias(libs.plugins.kotlin) apply false
}

/*
    Hilt classpath config, remove it if don't use hilt
 */
buildscript {
    dependencies {
        if (libs.plugins.hilt.android.gradle.isPresent) {
            val hiltPlugin = libs.plugins.hilt.android.gradle.get()
            val hiltClasspath = "${hiltPlugin.pluginId}:${hiltPlugin.version}"
            classpath(hiltClasspath)
        }
    }
}


tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

apply(from = "gradle/projectDependencyGraph.gradle")