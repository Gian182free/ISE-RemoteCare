plugins {
    id("java")
    id("application")
}

group = "com.remotecare"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Interprete Jason BDI
    implementation("io.github.jason-lang:jason-interpreter:3.2.0")

    // tuProlog per la base di conoscenza clinica
    implementation("it.unibo.alice.tuprolog:tuprolog:3.3.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("remotecare.RunSystem")
}