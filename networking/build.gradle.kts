plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("com.example.buildlogic.common")
}

android {
    namespace = "com.dre.project.networking"
}

dependencies {
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.serialization.converter)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.lib)
    implementation(libs.okhttp.interceptor)
}