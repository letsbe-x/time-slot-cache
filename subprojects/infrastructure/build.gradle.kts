dependencies {
	compileOnly(project(":domain"))
	runtimeOnly("com.h2database:h2")
}

tasks {
	bootJar {
		enabled = false
	}
	jar {
		enabled = true
	}
}
