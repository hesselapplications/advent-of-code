plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/utils", "src/2023", "src/2024")
        }
    }

    wrapper {
        gradleVersion = "8.4"
    }
}
