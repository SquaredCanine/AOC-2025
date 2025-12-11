plugins {
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
}

dependencies { 
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("tools.aqua:z3-turnkey:4.14.1")
}

kotlin {
    jvmToolchain(21)
}