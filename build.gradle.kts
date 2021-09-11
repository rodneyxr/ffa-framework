plugins {
    java
    `java-library-distribution`
    `maven-publish`
}

group = "edu.utsa.fileflow"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.antlr/antlr4-runtime
    implementation("org.antlr:antlr4-runtime:4.5")
    implementation("com.github.rodneyxr:ffa-grammar:main-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

distributions {
    main {
        distributionBaseName.set("framework")
    }
}

publishing {
    publications {
        create<MavenPublication>("framework") {
            artifact(tasks.jar)
        }
    }
}