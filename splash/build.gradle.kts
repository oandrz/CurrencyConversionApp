plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    id("com.example.buildlogic.common")
    id("com.example.buildlogic.hilt")
    id("com.example.buildlogic.compose")
}

android {
    namespace = "com.example.splash"

    buildFeatures {
        compose = true
    }
}

dependencies {


}