![](https://github.com/android-tut-study/android-gradle-template/workflows/check/badge.svg)

# android-gradle-template

The simple gradle plugin implementation for Android multi modules application.

## How to use

Just click
on [![Use this template](https://img.shields.io/badge/-Use%20this%20template-brightgreen)](https://github.com/android-tut-study/android-gradle-template/generate)
button to create a new repo starting from this template.

## Features

- 100% Kotlin-only template.
- Plugin build setup with **[composite build](https://docs.gradle.org/current/userguide/composite_builds.html)**.
- Dependency versions managed via Gradle Versions Catalog (`libs.versions.toml`).
- Support [Hilt](https://dagger.dev/hilt/), Android Application plugin for configuration

## Android Configuration

- Currently, this template support Application and Library module configuration. Android default configuration gotten
  from `rootProject/gradle/config/app.properties`. Modify it for your application requirement:

```properties
# Application Properties
applicationId=com.example.android.gradle.template
versionCode=1
versionName=1.0
supportVectorDrawableLibrary=true
# Default Configuration
minSdk=21
targetSdk=32
compileSdk=32
testInstrumentationRunner=androidx.test.runner.AndroidJUnitRunner
```

- With `application` module we use `app-plugin` and with `library` modules, we use `android-lib-plugin`
```kotlin
// app/build.gradle.kts
plugins {
  // other plugins
  id("app-plugin")
}

// ui:ui-module/build.gradle.kts
plugins {
  // other plugins
  id("android-lib-plugin")
}
```

- If we need [Jetpack compose](https://developer.android.com/jetpack/compose) support, just add below extension config to your build script:
```kotlin
uiConfiguration { // add this extension configuration
    applyCompose() // call this function to apply jetpack compose
}
```

## Hilt configuration

Please follow these steps to use `hilt-plugin`

- This template use `version catalog`. So we need to make sure hilt dependencies exist:

```toml
[versions]
...
hilt = "2.43.2" # Update this version to change hilt version 
...
[plugins]
...
hilt-android-gradle = { id = "com.google.dagger:hilt-android-gradle-plugin", version.ref = "hilt" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
...
# Hilt
[libraries]
###
hiltLibs-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hiltLibs-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hiltLibs-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hiltLibs-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
```

- We also need to add hilt `classpath` to project level build script (refer to https://dagger.dev/hilt/gradle-setup)

```kotlin
// rootProject/build.gradle.kts
buildscript {
    dependencies {
        if (libs.plugins.hilt.android.gradle.isPresent) {
            val hiltPlugin = libs.plugins.hilt.android.gradle.get() // Get Hilt Gradle Plugin from version catalog
            val hiltClasspath = "${hiltPlugin.pluginId}:${hiltPlugin.version}" // create classpath dependency name
            classpath(hiltClasspath)
        }
    }
}
```

- Now we can use `hilt-plugin` in any project modules:

```kotlin
plugins {
    // other plugins
    id("hilt-plugin")
}
```

- Currently, this template also support to
  add [Hilt Navigation Compose](https://developer.android.com/jetpack/androidx/releases/hilt#hilt-navigation-compose_version_100_2)
  . To apply it, just add to your module buildScript:

```kotlin
hiltConfiguration {
    navigation { applied.set(true) }
}
```

### Caution
With custom android plugins, if we use `@HiltAndroidApp` follow [Hilt Gradle Setup](https://dagger.dev/hilt/gradle-setup) for your Application class. IDE can't detect your multidex enable or not. So it will show unresolved `MultiDexApplication`. We also need to add multidex dependencies follow (https://developer.android.com/studio/build/multidex#mdex-pre-l) 

```kotlin
// app/build.gradle.kts
dependencies {
  // other dependencies
  implementation("androidx.multidex:multidex:2.0.1")
}
```
