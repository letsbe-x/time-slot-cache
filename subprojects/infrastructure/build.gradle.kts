dependencies {
	compileOnly(project(":domain"))
}

tasks {
	bootJar {
		enabled = false
	}
	jar {
		enabled = true
	}
}
