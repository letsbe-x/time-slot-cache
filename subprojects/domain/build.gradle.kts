dependencies {
}

allOpen {
	annotation("javax.persistence.Entity")
}

tasks {
	bootJar {
		enabled = false
	}
	jar {
		enabled = true
	}
}
