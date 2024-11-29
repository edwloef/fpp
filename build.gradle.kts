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
    if (hasProperty("launch")) {
        when ("${property("launch")}") {
            "chatServer" -> {
                mainClass.set("chat.ChatServer")
            }
            "chatClient" -> {
                mainClass.set("chat.ChatClient")
            }
        }
    }
}

tasks.getByName("run", JavaExec::class) {
    standardInput = System.`in`
}
