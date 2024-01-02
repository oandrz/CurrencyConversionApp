plugins {
    val kt = "1.9.21"
    kotlin("jvm").version(kt)
    id("java-gradle-plugin")
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

gradlePlugin {
    plugins {
        register("commonDependencies") {
            id = "com.example.buildlogic.common"
            implementationClass = "com.example.buildlogic.CommonDependenciesPlugin"
        }
        register("androidHilt") {
            id = "com.example.buildlogic.hilt"
            implementationClass = "com.example.buildlogic.AndroidHiltConventionPlugin"
        }
        register("compose") {
            id = "com.example.buildlogic.compose"
            implementationClass = "com.example.buildlogic.ComposeConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}