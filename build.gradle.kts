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
    implementation("io.vavr:vavr:0.10.4")
    implementation("org.scala-lang:scala3-library_3:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.github.yjgbg:fun-valid:ROLLING-PARALLEL-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
