package com.example.buildlogic

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<BaseExtension> {
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.7"
                }
            }

            dependencies {
                add("implementation", project.dependencies.platform("androidx.compose:compose-bom:2022.10.00"))
//                "implementation"(libs.findLibrary("androidx.compose.bom").get())
                "implementation"(libs.findLibrary("androidx.compose.activity").get())
                "implementation"(libs.findLibrary("androidx.compose.ui.graphics").get())
                "implementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                "implementation"(libs.findLibrary("androidx.compose.material3").get())
                "implementation"(libs.findLibrary("material").get())
            }
        }
    }
}