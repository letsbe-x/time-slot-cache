dependencies {
// 	compileOnly(project(":domain"))
	api(project(":domain"))
	runtimeOnly("com.h2database:h2")
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
