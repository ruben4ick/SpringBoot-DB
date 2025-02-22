	plugins {
		java
		id("org.springframework.boot") version "3.4.3"
		id("io.spring.dependency-management") version "1.1.7"
	}

	group = "ua.ukma"
	version = "0.0.1-SNAPSHOT"

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
		implementation("org.springframework.boot:spring-boot-starter-web")
		compileOnly("org.projectlombok:lombok")
		developmentOnly("org.springframework.boot:spring-boot-docker-compose")
		runtimeOnly("com.h2database:h2")
		runtimeOnly("org.postgresql:postgresql")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.boot:spring-boot-testcontainers")
		testImplementation("org.testcontainers:junit-jupiter")
		testImplementation("org.testcontainers:postgresql")
		testImplementation("org.mockito:mockito-core:5.6.0")
		testImplementation("org.mockito:mockito-junit-jupiter:5.6.0")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

