plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
    id("com.example.buildlogic.common")
    id("com.example.buildlogic.hilt")
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

    packaging {
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

    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.testJunit)
    testImplementation(libs.testKotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutine.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.espresso)

    androidTestImplementation(libs.testAndroidCore)
    androidTestImplementation(libs.testAndroidHilt)
    androidTestImplementation(libs.testAndroidRunner)

    kaptAndroidTest(libs.testAndroidHiltCompiler)
}