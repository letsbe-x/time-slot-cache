import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val groupName: String by project
val currentVersion: String by project

allprojects {
	group = groupName
	version = currentVersion

	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"
	id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

subprojects {
	apply {
		plugin("kotlin")
		plugin("kotlin-spring") // all-open
		plugin("org.jetbrains.kotlin.plugin.jpa") // no-arg

		plugin("org.springframework.boot")
		plugin("io.spring.dependency-management")
		plugin("org.jlleitschuh.gradle.ktlint")
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("org.springframework.boot:spring-boot-starter-webflux")
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")

		runtimeOnly("org.jetbrains.kotlin:kotlin-reflect") // reflection

// 		developmentOnly("org.springframework.boot:spring-boot-docker-compose")
		developmentOnly("org.springframework.boot:spring-boot-devtools")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
	imageName.set("<docker username/image name>:<version>")
}

configure<KtlintExtension> {
	filter {
		exclude {
			it.file.path.contains("generated")
		}
	}
}

tasks {
	jar {
		enabled = true
	}
	bootJar {
		enabled = false
	}
}
