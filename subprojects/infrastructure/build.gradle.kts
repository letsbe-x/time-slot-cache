dependencies {
	api(project(":domain"))
	runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("io.lettuce:lettuce-core:6.3.0.RELEASE")
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

tasks {
	bootJar {
		enabled = false
	}
	jar {
		enabled = true
	}
}
