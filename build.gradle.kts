plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("io.arrow-kt:arrow-core:1.2.0")
//    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
