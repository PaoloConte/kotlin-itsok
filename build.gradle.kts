import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
}

group = "io.paoloconte"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
    jvm()
    js(IR) {
        browser {
            useCommonJs()
            binaries.library()
        }
        nodejs()
    }
    linuxX64()
    linuxArm64()
    mingwX64()
    iosX64()
    iosArm64()
    watchosArm64()
    watchosSimulatorArm64()
    iosSimulatorArm64()
    macosArm64()
    macosX64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
        }

        named("commonMain") {
            dependencies {
            }
        }

    }
}