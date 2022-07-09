import io.gitlab.arturbosch.detekt.DetektPlugin

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
    }
}

plugins {
    id("com.android.application") version "7.2.0-rc02" apply false
    id("com.android.library") version "7.2.0-rc02" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("io.gitlab.arturbosch.detekt") version "1.21.0-RC2"
}

subprojects {
    apply<DetektPlugin>()

    detekt {
        buildUponDefaultConfig = true
        config = files("$rootDir/detekt.yml")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
