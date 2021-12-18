
dependencies {
    implementation(project(":core"))
    implementation(project(":support"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // swagger
    implementation("org.springdoc:springdoc-openapi-ui:1.6.1")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.1")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = true
    }
}