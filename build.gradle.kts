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
    mainClass = "chat.ChatServer"
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}
