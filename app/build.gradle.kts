plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("com.example.buildlogic.common")
    id("com.example.buildlogic.hilt")
    id("com.example.buildlogic.compose")
}

android {
    namespace = "com.dre.project"

    defaultConfig {
        applicationId = "com.dre.project"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":design"))
    implementation(project(":networking"))
    implementation(project(":splash"))
    implementation(project(":rate"))

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.compose.activity)

    implementation(libs.coil)
    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.testJunit)
    testImplementation(libs.testKotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutine.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.espresso)

    androidTestImplementation(libs.androidx.compose.junit4)
    androidTestImplementation(libs.testAndroidCore)
    androidTestImplementation(libs.testAndroidHilt)
    androidTestImplementation(libs.testAndroidRunner)

    kaptAndroidTest(libs.testAndroidHiltCompiler)

    debugImplementation(libs.androidx.compose.ui.tooling)
}