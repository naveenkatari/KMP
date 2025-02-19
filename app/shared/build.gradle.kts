import org.gradle.api.internal.catalog.DelegatingProjectDependency
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
}

val libName = "CurrencyExchangeKMP"
val repo = "" // FIXME: add repo here
group = "com.mindera.kmpexample"
version = "1.0.0"

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.framework(
    name: String,
    configure: org.jetbrains.kotlin.gradle.plugin.mpp.Framework.() -> Unit,
) {
    val xcFramework = XCFramework(name)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework(name) {
            freeCompilerArgs += listOf(
                "-Xexport-kdoc",
            )
            xcFramework.add(this)
            configure()
        }
    }
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "https://github.com/naveenkatari/KMP.git"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "shared"
        }
    }
}

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.framework(
    name: String,
    modules: List<DelegatingProjectDependency>,
    configure: org.jetbrains.kotlin.gradle.plugin.mpp.Framework.() -> Unit,
) {
    iOS(enabled = {
        framework(name) {
            configure()
            modules.forEach(::export)
        }
    }, disabled = { jvm() })

    sourceSets {
        commonMain {
            dependencies {
                modules.forEach(::api)
            }
        }
    }
}

private val modules: List<DelegatingProjectDependency> = listOf(
    projects.features.launches.data,
    projects.features.launches.data.remote.mindera.rest.ktor,
    projects.features.launches.domain,
    projects.common.api.mindera.rest.ktor,
    projects.common.api.mindera.rest.ktor.interceptors,
    projects.common.coroutinesKtx,
    projects.common.domain,
    projects.features.launches.di,
)

kotlin {
    framework(libName, modules) {
//        linkerOpts.add("-lsqlite3")
    }
    tasks {
        registerPublish(libName, type = "Debug", repo, branch = "main")
        registerPublish(libName, type = "Release", repo, branch = "main")
    }
}
