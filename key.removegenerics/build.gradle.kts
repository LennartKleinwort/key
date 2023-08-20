import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":key.core"))
}

application {
    mainClass.set("de.uka.ilkd.key.util.removegenerics.Main")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier = "exe"
    archiveBaseName = "key.removegenerics"
}
