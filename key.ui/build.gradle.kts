import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.nio.file.Files
import java.nio.file.Paths

plugins {
    application
    // Used to create a single executable jar file with all dependencies
    // see task "shadowJar" below
    // https://imperceptiblethoughts.com/shadow/
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":key.core"))
    implementation(project(":key.core:rifl"))

    implementation(project(":key.core.proof_references"))
    implementation(project(":key.core.symbolic_execution"))
    implementation(project(":key.removegenerics"))

    api("com.miglayout:miglayout-swing:11.1")

    //logging implementation used by the slf4j
    implementation("ch.qos.logback:logback-classic:1.4.8")

    api("org.key-project:docking-frames-common:1.1.3p1")
    api("org.key-project:docking-frames-core:1.1.3p1")

    runtimeOnly(project(":keyext.ui.testgen"))
    runtimeOnly(project(":keyext.exploration"))
    runtimeOnly(project(":keyext.slicing"))
    runtimeOnly(project(":keyext.proofmanagement"))
}

val createExamplesZip by tasks.registering(Zip::class) {
    description = "Create 'examples.zip' containing all KeY examples"
    destinationDirectory = file("$buildDir/resources/main/")
    archiveFileName = "examples.zip"
    from("examples")
    include(scanReadmeFiles())  // see end of file
}

tasks.getByName("processResources").dependsOn.add(createExamplesZip)

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier = "exe"
    archiveBaseName = "key"
    mergeServiceFiles()
}

tasks.named<Zip>("distZip") {
    archiveBaseName = "key"
}

application {
    mainClass.set("de.uka.ilkd.key.core.Main")
}

tasks.named<JavaExec>("run") {
    systemProperties["key.examples.dir"] = "$projectDir/examples"
    //systemProperties["slf4j.detectLoggerNameMismatch"] = true
    //systemProperties["KeyDebugFlag"] = "on"
    //args "--experimental"

    // this can be used to solve a problem where the OS hangs during debugging of popup menus
    // (see https://docs.oracle.com/javase/10/troubleshoot/awt.htm#JSTGD425)
    jvmArgs("-Dsun.awt.disablegrab=true")
}

tasks.register<JavaExec>("runWithProfiler") {
    group = "application"
    description = """Run key.ui with flight recoder enabled. 
                   See https://www.baeldung.com/java-flight-recorder-monitoring for a how to."""

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("de.uka.ilkd.key.core.Main")
    jvmArgs = listOf(
        "-XX:+FlightRecorder",
        "-XX:StartFlightRecording=duration=30s,filename=key_profile.jfr",
        "-XX:FlightRecorderOptions=stackdepth=256"
    )
    doLast {
        println("A file key_profile.jfr has been created in folder 'key.ui' by JRE (FlightRecoder).")
        println("You can open key_profile.jfr in IntelliJ to inspect the performance measurement.")
    }
}

/**
 * Go and scan all registered README.txt files for files that need to be
 * included into examples.zip. Thus only those files that can actually be
 * accessed from the example browser are added to examples.zip.
 *
 * The file size is reduced considerably.
 *
 * @return a list of filenames to be added to examples.zip. Each line
 * is relative to the examples directory.
 */
fun scanReadmeFiles(): List<String> {
    val examplesIndex = Paths.get(projectDir.toString(), "examples", "index", "samplesIndex.txt")
    val result = ArrayList<String>()
    result.add("index/samplesIndex.txt")
    for (line in Files.readAllLines(examplesIndex)) {
        val line = line.trim()
        if (line.isEmpty() || line.startsWith("#")) {
            continue
        }
        val readme = Paths.get(projectDir.toString(), "examples", line.replace("/", File.separator))
        val dir = line.substring(0, line.lastIndexOf('/') + 1)
        result.add(line)
        // The project file is not always project.key, but better include the default file.
        result.add(dir + "project.key")
        for (rmLine in Files.readAllLines(readme)) {
            if (rmLine.startsWith("#")) continue
            val (key, filename) = rmLine.split("=")
            if (key == "file") {
                result.add(dir + filename)
            }
        }
    }
    // println(result)
    return result
}
