group = "space.sentinel"
version = "1.0-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.3.40"
}

application {
    mainClassName = "space.hajnal.App"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.projectreactor:reactor-core:3.3.0.RELEASE")
    implementation("org.slf4j:slf4j-api:1.7.26")
    implementation("ch.qos.logback:logback-classic:0.9.26")
    implementation("ch.qos.logback:logback-core:0.9.26")
    implementation("javax.xml.bind:jaxb-api:2.3.0")
    implementation("org.bytedeco:javacv-platform:1.5.2")
    implementation("io.github.config4k:config4k:0.4.1")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.9")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("io.projectreactor:reactor-test:3.3.0.RELEASE")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

repositories {
    jcenter()
}

val fatJar = task("fatJar", type = Jar::class) {
    manifest {
        attributes["Implementation-Title"] = "PIR Sensor Reader"
        attributes["Implementation-Version"] = "1.0"
        attributes["Main-Class"] = "space.sentinel.AppKt"
    }
    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

tasks {

    "build" {
        dependsOn(fatJar)
    }
    "test"(Test::class) {
        useJUnitPlatform()
    }

}