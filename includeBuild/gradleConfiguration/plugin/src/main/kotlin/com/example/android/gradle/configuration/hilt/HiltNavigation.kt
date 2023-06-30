package com.example.android.gradle.configuration.hilt

import org.gradle.api.provider.Property

abstract class HiltNavigation {
    abstract val composeVersion: Property<String>
    abstract val applied: Property<Boolean>
}
