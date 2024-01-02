package com.example.buildlogic

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class CommonDependenciesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKotlin()
            configureAndroid()

            dependencies {
                "implementation"(libs.findLibrary("timber").get())
            }
        }
    }

    private fun Project.configureKotlin() {
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    private fun Project.configureAndroid() {
        extensions.configure<BaseExtension> {
            compileSdkVersion(34)
            defaultConfig {
                minSdk = 24
                targetSdk = 34
                vectorDrawables.useSupportLibrary = true
                compileOptions {
                    targetCompatibility = JavaVersion.VERSION_17
                    sourceCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}