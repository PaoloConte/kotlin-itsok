
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

repositories {
    mavenCentral()
}

mavenPublishing {
    configure(KotlinMultiplatform(
        javadocJar = JavadocJar.Empty(),
        sourcesJar = true,
    ))

    coordinates(project.group.toString(), "kotlin-itsok-kotest", project.version.toString())

    pom {
        name.set("Kotlin ItsOk kotest extensions")
        description.set("Kotest utility extensions for the main ItsOk library")
        inceptionYear.set("2024")
        url.set("https://github.com/PaoloConte/kotlin-itsok")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/PaoloConte/kotlin-itsok/blob/main/LICENSE")
                distribution.set("https://github.com/PaoloConte/kotlin-itsok/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("PaoloConte")
                name.set("Paolo Conte")
                url.set("https://github.com/PaoloConte/")
            }
        }
        scm {
            url.set("https://github.com/PaoloConte/kotlin-itsok")
            connection.set("scm:git:git@github.com:PaoloConte/kotlin-itsok.git")
            developerConnection.set("scm:git:ssh://git@github.com/PaoloConte/kotlin-itsok.git")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
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
    iosSimulatorArm64()
    watchosArm64()
    watchosSimulatorArm64()
    macosArm64()
    macosX64()
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
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
                implementation("io.paoloconte:kotlin-itsok:${project.version}")
                implementation("io.kotest:kotest-assertions-core:6.0.4")
            }
        }

    }
}