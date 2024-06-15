import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    application
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.0-RC1"
    id("org.jetbrains.dokka") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.22"
}

group = "edu.udo.cs.sopra.group.six"
version = "1.1.2"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://sopra-gitlab.cs.tu-dortmund.de/api/v4/projects/1285/packages/maven")
        credentials(HttpHeaderCredentials::class) {
            name = "Private-Token"
            value = "glpat-Enoru9Fp92RaG-cEzizT"
        }
        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("PokaniKt")
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    implementation(group = "tools.aqua", name = "bgw-gui", version = "0.7.3-14-6ab4c8e-SNAPSHOT")
    implementation(group = "tools.aqua", name = "bgw-net-common", version = "0.7.3")
    implementation(group = "tools.aqua", name = "bgw-net-client", version = "0.7.3")
    implementation(group = "edu.udo.cs.sopra", name = "ntf", version = "1.0")
    implementation("com.andreapivetta.kolor:kolor:1.0.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

tasks.distZip {
    archiveFileName.set("distribution.zip")
    destinationDirectory.set(layout.projectDirectory.dir("public"))
}

tasks.test {
    useJUnitPlatform()
    reports.html.outputLocation.set(layout.projectDirectory.dir("public/test"))
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.clean {
    delete.add("public")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.projectDirectory.dir("public/coverage"))
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude(listOf("view/**", "entity/**", "service/ai/**", "Pokani.kt"))
        }
    }))
}

//detekt {
//    reports {
//        // Enable/Disable HTML report (default: true)
//        html {
//            required.set(true)
//            reportsDir = file("public/detekt")
//        }
//
//        sarif {
//            required.set(false)
//        }
//    }
//}

tasks.dokkaHtml.configure {
    outputDirectory.set(projectDir.resolve("public/dokka"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}