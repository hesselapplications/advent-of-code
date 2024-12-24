plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}

tasks {
    sourceSets {
        create("2023") {
            java.srcDir("src/2023")
            compileClasspath += sourceSets.main.get().runtimeClasspath
        }
        create("2024") {
            java.srcDir("src/2024")
            compileClasspath += sourceSets.main.get().runtimeClasspath
        }
    }

    wrapper {
        gradleVersion = "8.4"
    }
}
