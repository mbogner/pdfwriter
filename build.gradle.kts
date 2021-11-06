import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    kotlin("jvm") version "1.5.31"
    // https://plugins.gradle.org/plugin/org.sonarqube
    id("org.sonarqube") version "3.3"
}

val javaVersion = JavaVersion.VERSION_11
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
val junitVersion = "5.8.1"

dependencies {
    implementation(kotlin("stdlib"))
    // https://mvnrepository.com/artifact/com.github.librepdf/openpdf
    implementation("com.github.librepdf:openpdf:1.3.26")
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.14.3")
    // https://mvnrepository.com/artifact/org.xhtmlrenderer/flying-saucer-pdf-openpdf
    implementation("org.xhtmlrenderer:flying-saucer-pdf-openpdf:9.1.22")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

allprojects {
    group = "com.openresearch"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion.toString()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.isIncremental = true
    }

    tasks.withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.withType<Wrapper> {
    // https://gradle.org/releases/
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.ALL
}

sonarqube {
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.projectKey", "pdfwriter")
        property("sonar.projectName", "OR::PdfWriter")
        property("sonar.sources", "src/main/kotlin,src/main/java,src/main/resources")
        property("sonar.exclusions", "**/src/gen/**/*,**/src-gen/**/*")
    }
}
