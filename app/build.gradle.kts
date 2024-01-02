plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("com.example.buildlogic.common")
}

android {
    namespace = "com.example.nutmegproj"

    defaultConfig {
        applicationId = "com.example.nutmegproj"
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

    plugins.withType<com.android.build.gradle.BasePlugin> {
        apply(plugin = "com.example.buildlogic.common")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(project(":networking"))
    implementation(project(":splash"))

    implementation(libs.dagger.hilt)
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    implementation(libs.appcompat)
    implementation(libs.material)
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.compose.activity)
    implementation(libs.coil)

    implementation(libs.kotlin.serialization.converter)

    implementation(libs.retrofit)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.timber)
    implementation(libs.androidx.viewmodel.lifecycle)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.lib)
    implementation(libs.okhttp.interceptor)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.kapt)

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