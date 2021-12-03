plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":support"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = true
    }
}