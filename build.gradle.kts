plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "DOPC"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web") // Web API support
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // JSON parsing
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
	implementation("org.springframework.boot:spring-boot-starter-validation") // Request validation
	implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin reflection
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("com.jayway.jsonpath:json-path:2.8.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test") // Spring Boot testing
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Kotlin test
	testRuntimeOnly("org.junit.platform:junit-platform-launcher") // For running tests
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
