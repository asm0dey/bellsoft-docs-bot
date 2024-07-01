plugins {
	alias(libs.plugins.org.springframework.boot)
	alias(libs.plugins.io.spring.dependency.management)
	alias(libs.plugins.org.jetbrains.kotlin.jvm)
	alias(libs.plugins.org.jetbrains.kotlin.plugin.spring)
	alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)

}

group = "com.github.asm0dey"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springAiVersion"] = "1.0.0-M1"

dependencies {
	implementation(libs.jackson.module.kotlin)
	implementation(libs.spring.ai.pgvector.store.spring.boot.starter)
	implementation(libs.spring.ai.openai.spring.boot.starter)
	implementation(libs.kotlin.reflect)
	implementation(libs.spring.boot.starter.web)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.kotlin.test.junit5)
	testRuntimeOnly(libs.junit.platform.launcher)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinx.coroutines.reactor)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.ktor.client.core)
	implementation(libs.ktor.client.java)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.ktor.serialization.kotlinx.json)
}


dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
