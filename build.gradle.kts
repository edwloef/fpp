plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass = "Main"
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}
