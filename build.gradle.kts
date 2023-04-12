plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.ms"
version = "1.09"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    implementation("com.jcraft:jsch:0.1.55")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.8.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("net.dv8tion:JDA:5.0.0-alpha.22")
}
tasks.shadowJar {
    project.setProperty("mainClassName","Main")
    archiveFileName.set("MDBot_v${project.version}.jar")
    doLast {
        val root = File("build/libs/system/")
            .mkdir()
        val file = File("build/libs/system/version.txt")
            .writeText("${project.version}")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}