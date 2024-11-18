plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.sun.mail:javax.mail:1.6.2")
}

application {
    mainClass = "spiele.Main"
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}
