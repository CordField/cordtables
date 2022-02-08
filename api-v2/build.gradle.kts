import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.asciidoctor.convert") version "1.5.8"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "com.cordtables"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["testcontainersVersion"] = "1.16.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.data:spring-data-rest-hal-explorer")
	runtimeOnly("software.aws.rds:aws-postgresql-jdbc:0.1.0")

	implementation("org.bouncycastle:bcprov-jdk15on:1.69")
	implementation("org.springframework.security:spring-security-crypto:5.5.2")

	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.5")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.5")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.5")
	implementation("org.springdoc:springdoc-openapi-javadoc:1.6.5")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.testcontainers:junit-jupiter")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
//
//tasks.test {
//	outputs.dir(snippetsDir)
//}

//tasks.asciidoctor {
//	inputs.dir(snippetsDir)
//	dependsOn(test)
//}
