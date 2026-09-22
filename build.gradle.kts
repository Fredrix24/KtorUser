plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Server core
    implementation("io.ktor:ktor-server-core:3.0.0")
    // Netty engine
    implementation("io.ktor:ktor-server-netty:3.0.0")
    // ContentNegotiation for JSON serialization
    implementation("io.ktor:ktor-server-content-negotiation:3.0.0")
    // kotlinx.serialization for JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}