plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
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
