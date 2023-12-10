dependencies {
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("com.letsbe.domain.annotation.AllOpen")
}

tasks {
	bootJar {
		enabled = false
	}
	jar {
		enabled = true
	}
}
