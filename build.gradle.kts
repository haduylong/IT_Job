plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.freefair.lombok") version "8.11"
}

group = "vn.hdl"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.4.1")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.data:spring-data-redis:3.4.1")
	implementation("io.lettuce:lettuce-core:6.5.2.RELEASE")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

	implementation("com.turkraft.springfilter:jpa:3.1.7")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
