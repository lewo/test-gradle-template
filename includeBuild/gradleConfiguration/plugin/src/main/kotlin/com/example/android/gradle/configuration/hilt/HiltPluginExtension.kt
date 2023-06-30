package com.example.android.gradle.configuration.hilt

import com.example.android.gradle.configuration.hilt.HiltNavigation
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class HiltPluginExtension {

    abstract val version: Property<String>

    @get:Nested
    abstract val navigation: HiltNavigation

    fun navigation(action: Action<HiltNavigation>) {
        action.execute(navigation)
    }
}
