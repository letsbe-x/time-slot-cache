rootProject.name = "letsbe-spring-template"

include(":domain", ":application", ":infrastructure")

rootProject.children.forEach { project ->
	project.projectDir = file("subprojects/${project.name}")
	assert(project.projectDir.isDirectory)
}
