dependencies {
    implementation(project(":key.core"))
    val ss = project(":key.core").dependencyProject.sourceSets["test"]
    testImplementation(ss.output)
}


tasks.withType<Test> {
    maxHeapSize = "4g"
    systemProperty("UPDATE_TEST_ORACLE", System.getProperty("UPDATE_TEST_ORACLE"))
    systemProperty("ORACLE_DIRECTORY", System.getProperty("ORACLE_DIRECTORY"))
}
