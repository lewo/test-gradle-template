plugins {
    id("app-plugin")
    id("hilt-plugin")
}

hiltConfiguration {
    /*
        Uncomment below when need navigation support
     */
    // navigation { applied.set(true) }
}

kapt {
    correctErrorTypes = true
}

uiConfiguration {
    /*
        Uncomment below when need Jetpack compose support
     */
//    applyCompose()
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.androidxLibs.core.ktx)
    implementation(libs.androidxLibs.appcompat)
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.android.material:material:1.5.0-alpha04")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
}