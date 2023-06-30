package com.example.android.gradle.configuration.hilt

import com.example.android.gradle.configuration.util.debug
import com.example.android.gradle.configuration.util.warning
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

const val Default_Hilt_Version = "2.43.2"
const val Default_Hilt_Classpath_Artifact = "hilt-android-gradle-plugin"
const val Default_Hilt_Navigation_Compose_Version = "1.0.0"

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val hiltLibs = target.extensions.getByType<VersionCatalogsExtension>().find("libs")

        // Check classPath configuration
        val hiltClassPathConfigName = if (hiltLibs.isPresent && hiltLibs.get().findPlugin("hilt-android-gradle").isPresent) {
            // Get classPath Artifact
            val classPathArtifact = hiltLibs.get().findPlugin("hilt-android-gradle").get().get().pluginId.split(":")[1]
            target.debug("found hilt classPath configuration with artifact $classPathArtifact")
            classPathArtifact
        } else Default_Hilt_Classpath_Artifact

        val classPath = target.rootProject.buildscript.configurations.named("classpath")
        val hasClassPathConfig = classPath.get().dependencies.find {
            it.name == hiltClassPathConfigName
        } != null

        if (!hasClassPathConfig) {
            target.warning("""
                Missing hilt classpath config at rootProject buildScript.
                Please follow https://dagger.dev/hilt/gradle-setup to add classPath
            """.trimIndent())
            return
        }
        // Hilt config
        val hiltExtension =
            target.extensions.create("hiltConfiguration", HiltPluginExtension::class.java)
        target.pluginManager.apply("kotlin-kapt")

        hiltLibs.ifPresentOrElse(
            { hiltLibCatalog ->
                applyHiltCoreDependencies(target, hiltLibCatalog)
            },
            {
                target.pluginManager.apply("com.google.dagger.hilt.android")
                applyHiltDefaultCoreDependencies(target)
            }
        )

        target.afterEvaluate {
            if (hiltExtension.version.isPresent) {
                debug("Forced Apply with [Hilt Extension Version]: ${hiltExtension.version.get()}")
                val hiltVersion = hiltExtension.version.get()
                applyHiltDefaultCoreDependencies(target, hiltVersion)
            }

            if (hiltExtension.navigation.applied.isPresent && hiltExtension.navigation.applied.get()) {
                // Apply navigation dependencies
                if (hiltExtension.navigation.composeVersion.isPresent) {
                    val navigationComposeVersion = hiltExtension.navigation.composeVersion.get()
                    debug("Apply Hilt Navigation Compose from gradle build config $navigationComposeVersion")
                    applyNavigationCompose(target, navigationComposeVersion)
                } else {
                    hiltLibs.ifPresentOrElse(
                        { hiltLibCatalog ->
                            hiltLibCatalog.findLibrary("hiltLibs-navigation-compose").ifPresentOrElse(
                                { target.dependencies.add("implementation", it) },
                                {
                                    debug("Not found [hiltLibs-navigation-compose] Library in version catalog! Apply Default Hilt Navigation Compose")
                                    applyNavigationCompose(
                                        target,
                                        Default_Hilt_Navigation_Compose_Version
                                    )
                                }
                            )
                        },
                        {
                            debug("Not found version catalog config! Apply Default Hilt Navigation Compose")
                            applyNavigationCompose(
                                target,
                                Default_Hilt_Navigation_Compose_Version
                            )
                        }
                    )
                }
            }
        }
    }

    private fun applyNavigationCompose(project: Project, version: String) {
        project.dependencies.add(
            "implementation",
            "androidx.hilt:hilt-navigation-compose:$version"
        )
    }

    private fun applyHiltCoreDependencies(
        target: Project,
        hiltLibs: VersionCatalog
    ) {
        target.debug(
            "Apply Hilt Core Dependencies with VersionCatalog ${
                hiltLibs.findVersion("hilt").get()
            }"
        )
        target.pluginManager.apply {
            hiltLibs.findPlugin("hilt-android").ifPresent { hiltPlugin ->
                apply(hiltPlugin.get().pluginId)
            }
        }

        target.dependencies.apply {
            add("implementation", hiltLibs.findLibrary("hiltLibs-android").get())
            add("kapt", hiltLibs.findLibrary("hiltLibs-compiler").get())
        }
    }

    private fun applyHiltDefaultCoreDependencies(target: Project, forcedVersion: String? = null) {
        var resolvedVersion = forcedVersion ?: Default_Hilt_Version
        if (resolvedVersion.toFloat() < Default_Hilt_Version.toFloat()) {
            target.warning("Current Hilt Setting is $forcedVersion")
            target.warning("Please make sure Hilt Version in gradle build larger than $Default_Hilt_Version >>> Apply Default Hilt Version $Default_Hilt_Version")
            resolvedVersion = Default_Hilt_Version
        }
        target.debug("Apply Hilt Dependencies with version $resolvedVersion")
        target.dependencies.apply {
            add("implementation", "com.google.dagger:hilt-android:$resolvedVersion")
            add("kapt", "com.google.dagger:hilt-compiler:$resolvedVersion")
        }
    }
}
