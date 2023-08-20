import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.eclipse.model.SourceFolder
import java.util.*

plugins {
    //Support for IntelliJ IDEA
    //https://docs.gradle.org/current/userguide/idea_plugin.html
    id("idea")

    //Support for Eclipse
    //https://docs.gradle.org/current/userguide/eclipse_plugin.html
    id("eclipse")  //support for Eclipse

    //Checks and adds the license header of the source files:
    // Task: `licenseMain" and `licenseFormatMain"
    //https://github.com/hierynomus/license-gradle-plugin
    id("com.github.hierynomus.license-base") version "0.16.1"
    //Generates reports on the license of used packages: Task `downloadLicenses'
    //Some Licenses requires an entry in the credits (MIT, BSD)
    id("com.github.hierynomus.license-report") version "0.16.1"

    // Gives `gradle dependencyUpdate` to show which dependency has a newer version
    // id "com.github.ben-manes.versions" version "0.39.0"

    // Code formatting
    id("com.diffplug.spotless") version "6.16.0"


    //
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

// Configure this project for use inside IntelliJ:
idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = true
    }
}

// The $BUILD_NUMBER is an environment variable set by Jenkins.
val build = System.getenv("BUILD_NUMBER")?.let { "-$it" } ?: ""

group = "org.key-project"
version = "2.13.0$build"

subprojects {
    apply<JavaPlugin>()
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply<MavenPublishPlugin>()        // GPG signing of artifacts, required by maven central
    apply<SigningPlugin>()
    apply<IdeaPlugin>()
    apply<EclipsePlugin>()
    apply<SpotlessPlugin>()
    apply<CheckstylePlugin>()
    apply<PmdPlugin>()

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    dependencies {
        val implementation by configurations
        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("com.google.code.findbugs:jsr305:3.0.2")

        val testImplementation by configurations
        testImplementation("ch.qos.logback:logback-classic:1.4.8")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
        testImplementation(project(":key.util"))

        // testCompileOnly("junit:junit:4.13.2")
        val testRuntimeOnly by configurations
        testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile>().configureEach {
        // Setting UTF-8 as the java source encoding.
        options.encoding = "UTF-8"
        // Setting the release to Java 17
        options.release = 17
    }

    tasks.withType<Javadoc>().configureEach {
        isFailOnError = false
        val doclet = options as StandardJavadocDocletOptions
        doclet.addBooleanOption("Xdoclint:none", true)
        //options.verbose()
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible) {
            doclet.addBooleanOption("html5", true)
        }
    }

/*    val sourcesJar by tasks.registering(Jar::class) {
        description = "Create a jar file with the sources from this project"
        println(extensions.getByType(SourceSetContainer::class))
        //from(the<SourceSetContainer>()["main"].allJava)
        archiveClassifier = "sources"
    }

    val javadocJar by tasks.registering(Jar::class) {
        description = "Create a jar file with the javadocs from this project"
        from(tasks.named("javadoc"))
        archiveClassifier = "javadoc"
    }
*/

    val publishing = the(PublishingExtension::class)
    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                artifact(tasks.named("sourcesJar"))
                artifact(tasks.named("javadocJar"))

                pom {
                    name = project.name
                    description = project.description
                    url = "https://key-project.org/"

                    licenses {
                        license {
                            name = "GNU General Public License (GPL), Version 2"
                            url = "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html"
                        }
                    }

                    developers {
                        developer {
                            id = "key"
                            name = "KeY Developers"
                            email = "support@key-project.org"
                        }
                    }
                    scm {
                        connection = "scm:git:git://git.key-project.org/key/key.git"
                        developerConnection = "scm:git:git://git.key-project.org/key/key.git"
                        url = "https://git.key-project.org/"
                    }
                }
            }
        }
        repositories {
            maven {
                /**
                 * To be able to publish things on Maven Central, you need two things:
                 *
                 * (1) a JIRA account with permission on group-id `org.key-project`
                 * (2) a keyserver-published GPG (w/o sub-keys)
                 *
                 * Your `$HOME/.gradle/gradle.properties` should like this:
                 * ```
                 * signing.keyId=YourKeyId
                 * signing.password=YourPublicKeyPassword
                 * ossrhUsername=your-jira-id
                 * ossrhPassword=your-jira-password
                 * ```
                 *
                 * You can test signing with `gradle sign`, and publish with `gradle publish`.
                 * https://central.sonatype.org/publish/publish-guide/
                 */
                if (project.version.toString().endsWith("-SNAPSHOT")) {
                    name = "mavenSnapshot"
                    url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

                    credentials.username = project.properties.getOrDefault("ossrhUsername", "").toString()
                    credentials.password = project.properties.getOrDefault("ossrhPassword", "").toString()
                } else {
                    name = "mavenStaging"
                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials.username = project.properties.getOrDefault("ossrhUsername", "").toString()
                    credentials.password = project.properties.getOrDefault("ossrhPassword", "").toString()
                }
            }
        }
    }

    configure<SigningExtension> {
        useGpgCmd() // works better than the Java implementation, which requires PGP keyrings.
        sign(publishing.publications.getByName("mavenJava"))
    }

    tasks.withType<JavaCompile>().configureEach {
        // Setting UTF-8 as the java source encoding.
        options.encoding = "UTF-8"
        // Setting the release to Java 17
        options.release = 17
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<Javadoc>().configureEach {
        isFailOnError = false
        val doclet = options as StandardJavadocDocletOptions
        doclet.addBooleanOption("Xdoclint:none", true)
        //options.verbose()
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible) {
            doclet.addBooleanOption("html5", true)
        }
    }

    tasks.withType<Test>().configureEach {//Configure all tests
        systemProperty("test-resources", "src/test/resources")
        systemProperty("testcases", "src/test/resources/testcase")
        systemProperty("TACLET_PROOFS", "tacletProofs")
        systemProperty("EXAMPLES_DIR", file("$rootProject/key.ui/examples"))
        systemProperty("RUNALLPROOFS_DIR", "$buildDir/report/runallproves")

        systemProperty("key.disregardSettings", "true")
        maxHeapSize = "4g"

        forkEvery = 0 //default
        maxParallelForks = 1 // weigl: test on master

        useJUnitPlatform {
            //includeEngines("junit-vintage")
            includeEngines("junit-jupiter")
        }

        testLogging {
            // set options for log level LIFECYCLE
            events(FAILED)
            exceptionFormat = TestExceptionFormat.SHORT
            showStandardStreams = false

            // set options for log level DEBUG
            debug {
                events = setOf(FAILED, SKIPPED, STARTED)
                exceptionFormat = TestExceptionFormat.FULL
                showStandardStreams = true
            }

            // remove standard output/error logging from --info builds
            // by assigning only 'failed' and 'skipped' events
            info {
                events = setOf(FAILED, SKIPPED)
                showStandardStreams = true
            }
        }
    }

    tasks.named<Test>("test") {
        // Before we switched to JUnit 5, we used JUnit 4 with a customized discovery of test class.
        // This discovery called AutoSuite and searched in the compiled classes. AutoSuite was
        // necessary due to bugs caused in some execution order.
        // AutoSuites made the test order deterministic. A last known commit to find AutoSuite (for the case),
        // is 980294d04008f6b3798986bce218bac2753b4783.

        useJUnitPlatform {
            excludeTags("owntest", "interactive", "performance")
        }

        testLogging {
            events("failed")
        }
    }

    val sourceSets = the<JavaPluginExtension>().sourceSets
    val testFast by tasks.registering(Test::class) {
        group = "verification"
        useJUnitPlatform {
            excludeTags("slow", "performance", "interactive")
        }
    }

    // The following two tasks can be used to execute main methods from the project
    // The main class is set via "gradle -DmainClass=... execute --args ..."
    // see https://stackoverflow.com/questions/21358466/gradle-to-execute-java-class-without-modifying-build-gradle
    tasks.register<JavaExec>("execute") {
        description =
            "Execute main method from the project. Set main class via 'gradle -DmainClass=... execute --args ...'"
        group = "application"
        mainClass.set(System.getProperty("mainClass"))
        classpath = sourceSets["main"].runtimeClasspath
    }

    tasks.register<JavaExec>("executeInTests") {
        description =
            "Execute main method from the project (tests loaded). Set main class via 'gradle -DmainClass=... execute --args ...'"
        group = "application"
        mainClass.set(System.getProperty("mainClass"))
        classpath = sourceSets["test"].runtimeClasspath
    }

    configure<PmdExtension> {
        tasks.named<Pmd>("pmdTest") {
            isEnabled = false
        }

        isIgnoreFailures = true
        toolVersion = "6.53.0"
        isConsoleOutput = false
        rulesMinimumPriority = 5
        ruleSets = listOf("category/java/errorprone.xml", "category/java/bestpractices.xml")
    }

    val pmdMain by tasks.existing(Pmd::class)
    tasks.register<Pmd>("pmdMainChanged") {
        // Specify all files that should be checked
        val changedFiles = getChangedFiles()
        source(pmdMain.get().source.filter { f -> f.getAbsoluteFile().toString() in changedFiles })
        classpath = pmdMain.get().classpath
        reports {
            html.required = true
            html.outputLocation = file("build/reports/pmd/main_diff.html")
            xml.required = true
            xml.outputLocation = file("build/reports/pmd/main_diff.xml")
        }
    }

    configure<CheckstyleExtension> {
        toolVersion = "10.6.0"
        isIgnoreFailures = true
        configFile = file("$rootDir/gradle/key_checks.xml")
        isShowViolations = false // disable console output
    }

    val checkstyleMain by tasks.existing(Checkstyle::class)
    tasks.register<Checkstyle>("checkstyleMainChanged") {
        // Specify all files that should be checked
        val changedFiles = getChangedFiles()
        source(checkstyleMain.get().source.filter { f -> f.getAbsoluteFile().toString() in changedFiles })
        classpath = checkstyleMain.get().classpath

        // Define the output folder of the generated reports
        reports {
            html.required = true
            html.outputLocation = file("build/reports/checkstyle/main_diff.html")
            xml.required = true
            xml.outputLocation = file("build/reports/checkstyle/main_diff.xml")
        }
    }

    tasks.withType<Pmd> {
        reports {
            xml.required = true
            html.required = true
        }
    }

    configure<EclipseModel> { //configures the generated .project and .classpath files.
        classpath {
            file {
                whenMerged {
                    val cp = this as org.gradle.plugins.ide.eclipse.model.Classpath
                    // This adds the exclude entry for every resource and antlr folder.
                    //As eclipse is so stupid, that it does not distuinguish between resource and java folder correctly.
                    // entries.findAll { it.path.endsWith('src/test/antlr') }.each { it.excludes = ["**/*.java"] }
                    cp.entries
                        .filterIsInstance<SourceFolder>()
                        .filter { it.path.endsWith("/resources") }
                        .forEach { it.excludes = listOf("**/*.java") }
                }
            }
        }
    }

    configure<SpotlessExtension> {
        // see https://github.com/diffplug/spotless/tree/main/plugin-gradle

        // optional: limit format enforcement to just the files changed by this feature branch
        // ratchetFrom 'origin/master'

        format("Key") {
            // define the files to apply `misc` to
            //target '*.gradle', '*.md', '.gitignore'
            target("src/main/resources/**/*.key")
            trimTrailingWhitespace()
            //indentWithSpaces(4)       // this does not really work
            endWithNewline()
            // TODO: license headers are problematic at the moment,
            //  see https://git.key-project.org/key/key/-/wikis/KaKeY%202022-09-30
            //licenseHeaderFile("$rootDir/gradle/header", '\\s*\\\\\\w+')
        }

        antlr4 {
            target("src/*/antlr4/**/*.g4") // default value, you can change if you want
            //licenseHeaderFile "$rootDir/gradle/header"
        }

        java {
            //target("*.java")
            // don't need to set target, it is inferred from java

            // We ignore the build folder to avoid double checks and checks of generated code.
            targetExclude("build/**")

            // allows us to use spotless:off / spotless:on to keep pre-formatted sections
            toggleOffOn()

            removeUnusedImports()
            eclipse().configFile("$rootDir/scripts/tools/checkstyle/keyCodeStyle.xml")
            trimTrailingWhitespace()        // not sure how to set this in the xml file ...
            //googleJavaFormat().aosp().reflowLongStrings()

            // note: you can use an empty string for all the imports you didn't specify explicitly,
            // '|' to join group without blank line, and '\\#` prefix for static imports
            importOrder("java|javax", "de.uka", "org.key_project", "", "\\#")

            // specific delimiter: normally just 'package', but spotless crashes for files in default package
            // (see https://github.com/diffplug/spotless/issues/30), therefore 'import' is needed. '//' is for files
            // with completely commented out code (which would probably better just be removed in future).
            //licenseHeaderFile("$rootDir/gradle/header", '(package|import|//)')
        }
    }
}

val alldoc = tasks.registering(Javadoc::class) {
    group = "documentation"
    description = "Generate a JavaDoc across sub projects"
    val projects = subprojects
    //key.ui javadoc is broken
    source(projects.map { it.the<SourceSetContainer>()["main"].allJava })
    classpath = files(projects.map { it.the<SourceSetContainer>()["main"].compileClasspath })
    destinationDir = file("${buildDir}/docs/javadoc")

    with(options as StandardJavadocDocletOptions) {
        //showFromPrivate()
        addBooleanOption("html5", true)
        encoding = "UTF-8"
        addBooleanOption("Xdoclint:none", true)
        // overview = new File( projectDir, 'src/javadoc/package.html' )
        //stylesheetFile = new File( projectDir, 'src/javadoc/stylesheet.css' )
        windowTitle = "KeY API Documentation"
        docTitle = "KeY JavaDoc ($project.version) -- ${Date()}"
        bottom = "Copyright &copy; 2003-2023 <a href=\"https://key-project.org\">The KeY-Project</a>."
        isUse = true
        links = listOf(
            "https://docs.oracle.com/en/java/javase/17/docs/api/",
            "https://www.antlr.org/api/Java/"
        )
    }
}

// Creates a jar file with the javadoc over all sub projects.
tasks.register<Zip>("alldocJar") {
    dependsOn(alldoc)
    description = "Create a jar file with the javadoc over all sub projects"
    from(alldoc)
    archiveFileName = "key-api-doc-${project.version}.zip"
    destinationDirectory = file("$buildDir/distribution")
}


//conditionally enable jacoco coverage when `-DjacocoEnabled=true` is given on CLI.
val jacocoEnabled = System.getProperty("jacocoEnabled") ?: "false"
if (jacocoEnabled.toBoolean()) {
    project.logger.lifecycle("Jacoco enabled. Test performance will be slower.")
    apply(from = rootProject.file("scripts/jacocokey.gradle"))
}

fun getChangedFiles(): Set<String> {
    // Get the target and source branch
    val anchor = "git merge-base HEAD origin/main".executes()

    // Get list of all changed files including status
    val allFiles = "git diff --name-status --diff-filter=dr $anchor".execute()
    //println("Found ${allFiles.length} changed files")

    // Remove the status prefix
    val files = TreeSet<String>()
    for (file in allFiles) {
        if (file.length > 1) {
            val a = file.substring(1).trim()
            if (a.isNotBlank()) {
                files.add(("$rootDir/$a").toString())
            }
        }
    }
    // Return the list of touched files
    return files
}

fun String.executes(): String {
    return execute().joinToString("\n")
}


fun String.execute(): List<String> {
    val pb = ProcessBuilder(this.split(" "))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectErrorStream(true)
        .start()
    return pb.inputReader().lineSequence().toList()
}
