plugins {
    scala
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.github.yjgbg"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots") //maven snapshot
}

dependencies {
    implementation("org.typelevel:cats-core_3:2.6.1")
    implementation("org.scala-lang:scala3-library_3:3.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
