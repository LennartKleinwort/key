apply<JacocoPlugin>()


configure<JacocoPluginExtension> {
    toolVersion = "0.8.8"
}

subprojects {
    apply<JacocoPlugin>()

    tasks.named<JacocoReport>("jacocoTestReport") {
        reports {
            html.required = false
            xml.required = true
            csv.required = false
        }
    }

    tasks.named("test") {
        finalizedBy(tasks.named<JacocoReport>("jacocoTestReport"))
    }

    tasks.named<Test>("testFast") {
        finalizedBy(tasks.named<JacocoReport>("jacocoTestReport"))
        configure<JacocoPluginExtension> {
            this.reportsDirectory = file("$buildDir/jacoco/test.exec")
        }
    }
}