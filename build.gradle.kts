plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        create("2023") {
            java.srcDir("src/2023")
        }
        create("2024") {
            java.srcDir("src/2024")
        }
    }

    wrapper {
        gradleVersion = "8.4"
    }
}
