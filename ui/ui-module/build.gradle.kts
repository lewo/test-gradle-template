plugins {
    id("android-lib-plugin")
    id("hilt-plugin")
}

hiltConfiguration {
    /*
        Uncomment below when need navigation support
     */
    // navigation { applied.set(true) }
}

// Uncomment below to apply compose
/*uiConfiguration {
    applyCompose()
}*/

dependencies {

    implementation(libs.androidxLibs.core.ktx)
    implementation(libs.androidxLibs.appcompat)
    implementation("com.google.android.material:material:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
