plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.example.buildlogic.common")
    id("com.example.buildlogic.compose")
}

android {
    namespace = "com.dre.project.design"

    buildFeatures {
        compose = true
    }
}

dependencies {

}