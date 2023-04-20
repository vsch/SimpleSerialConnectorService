plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("org.jetbrains.intellij") version "1.13.3"
}

val pluginVersion = "1.0.0"
val pluginSinceBuild = "203.5981.155"
val pluginUntilBuild = ""

group = "com.vladsch.plugins"
version = pluginVersion

repositories {
    mavenLocal()
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2020.3")
    type.set("CL") // Target IDE Platform

    plugins.set(listOf())
}

dependencies {
    annotationProcessor("junit:junit:4.13.2")
    testImplementation("junit:junit:4.13.2")

    implementation("org.jetbrains:annotations:24.0.1")
    implementation("io.github.java-native:jssc:2.9.4") {
        exclude("org.slf4j", "slf4j-api")
    }
    compileOnly(files("lib/serial-monitor.jar"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    
    instrumentCode {
        enabled = true
    }

    setupInstrumentCode {
        enabled = true
    }

    buildSearchableOptions {
        enabled = false
    }
    
    runIde {
        enabled = false
    }

    patchPluginXml {
        version.set(pluginVersion)
        sinceBuild.set(pluginSinceBuild)
        untilBuild.set(pluginUntilBuild)
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
