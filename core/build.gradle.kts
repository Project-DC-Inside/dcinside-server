
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}