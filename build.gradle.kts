plugins {
    java
}

group = "com.remotecare"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/main/asl")
            srcDir("src/main/prolog")
        }
    }
    test { resources { srcDir("src/test/asl") } }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jason-lang:jason-interpreter:3.2.0") {
        exclude(group = "net.sf.ingenias", module = "jade")
    }
    implementation("it.unibo.alice.tuprolog:tuprolog:3.3.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

file(projectDir).listFiles()
    ?.filter { it.extension == "mas2j" }
    ?.forEach { mas2jFile ->
        val taskName = "run" + mas2jFile.nameWithoutExtension.replaceFirstChar { it.uppercase() } + "Mas"
        tasks.register<JavaExec>(taskName) {
            description = "Runs the MAS defined in ${mas2jFile.name} (single JVM, Local infrastructure)."
            group = "run"
            classpath = sourceSets.main.get().runtimeClasspath
            mainClass = "jason.infra.local.RunLocalMAS"
            args(mas2jFile.path)
            standardInput = System.`in`
            javaLauncher = javaToolchains.launcherFor(java.toolchain)
        }
    }