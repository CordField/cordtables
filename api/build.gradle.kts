import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.asciidoctor.convert") version "1.5.8"
	kotlin("jvm") version "1.5.30"
	kotlin("plugin.spring") version "1.5.30"
}

group = "com.seedcompany"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

extra["snippetsDir"] = file("build/generated-snippets")
extra["testcontainersVersion"] = "1.16.0"

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.bouncycastle:bcprov-jdk15on:1.69")
	implementation("org.springframework.security:spring-security-crypto:5.5.2")

	implementation("com.amazonaws:aws-java-sdk-ses:1.12.62")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("software.aws.rds:aws-postgresql-jdbc:0.1.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:selenium")
	testImplementation("org.seleniumhq.selenium:selenium-support:3.141.59")
	testImplementation("org.seleniumhq.selenium:selenium-remote-driver:3.141.59")
	testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:3.141.59")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "16"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//tasks.test {
//	outputs.dir(snippetsDir)
//}

//tasks.asciidoctor {
//	inputs.dir(snippetsDir)
//	dependsOn(test)
//}
