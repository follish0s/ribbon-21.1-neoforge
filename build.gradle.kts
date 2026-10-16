import org.embeddedt.embeddium.gradle.VerifyAPICompat

evaluationDependsOnChildren()

plugins {
    id("idea")
    id("java-library")
    id("maven-publish")
    id("embeddium-platform-selector")
}

operator fun String.invoke(): String {
    return (rootProject.properties[this] as String?)!!
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_21.majorVersion
    targetCompatibility = JavaVersion.VERSION_21.majorVersion
    options.encoding = "UTF-8"
}

version = "mod_version"()
group = "maven_group"()
println("Ribbon (Embeddium fork): $version")

base {
    archivesName = "ribbon"
}

repositories {
    maven("https://libraries.minecraft.net")
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://maven.tterrag.com/")
    maven("https://maven.blamejared.com")
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://maven.covers1624.net/")
}

configurations {
    val runtimeOnlyNonPublishable = create("runtimeOnlyNonPublishable") {
        description = "Runtime only dependencies that are not published alongside the jar"
        isCanBeConsumed = false
        isCanBeResolved = false
    }
    runtimeClasspath.get().extendsFrom(runtimeOnlyNonPublishable)
}

fun DependencyHandlerScope.compatCompileOnly(dependency: String) {
    "compatCompileOnly"(dependency)
}

dependencies {
    implementation("io.github.llamalad7:mixinextras-neoforge:0.3.5")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.googlecode.soundlibs:jlayer:1.0.1.4")
}

tasks.processResources {
    inputs.property("version", "version"())

    filesMatching(listOf("META-INF/neoforge.mods.toml", "fabric.mod.json")) {
        expand("version" to "version"())
    }
}

tasks.withType<JavaCompile> {
    options.release = 21
}

java {
    withSourcesJar()
}

tasks.create<Jar>("apiJar") {
    archiveClassifier = "api"
    from(sourceSets["main"].output) {
        exclude("org/embeddedt/embeddium/impl/**")
        exclude("assets/**")
    }
}

publishing {
    tasks.publish {
        dependsOn(tasks.build)
    }
    publications {
        this.create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named("apiJar"))
        }
    }
    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}

tasks.register<VerifyAPICompat>("verifyAPICompat") {
    group = "verification"
    binary = tasks.getByName<Jar>("jar").archiveFile
}

// ========= 把 JLayer 塞进 Ribbon 的 jar 里 =========
tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    val jlayerJar = configurations.runtimeClasspath.get().files.find {
        it.name.startsWith("jlayer")
    }
    if (jlayerJar != null) {
        from(zipTree(jlayerJar)) {
            exclude("META-INF/MANIFEST.MF")
            exclude("META-INF/*.SF")
            exclude("META-INF/*.DSA")
            exclude("META-INF/*.RSA")
        }
    }
}