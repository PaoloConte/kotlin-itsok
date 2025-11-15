rootProject.name = "kotlin-itsok"

include("lib")

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
