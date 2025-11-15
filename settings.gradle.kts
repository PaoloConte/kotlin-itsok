rootProject.name = "kotlin-itsok"

include("lib")
include("lib-kotest")

pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        val kotlinVersion: String by settings

        plugins {
            kotlin("multiplatform") version kotlinVersion
        }

    }

}
