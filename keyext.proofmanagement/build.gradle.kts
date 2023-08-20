import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("application")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":key.core"))
    implementation(project(":key.ui"))
    implementation("org.antlr:ST4:4.3.4")
}

application {
    mainClass = "org.key_project.proofmanagement.Main"
    // for start script generated with createStartScripts
    applicationName = "pm"
}

run {
    // for debugging, something like this can be used:
    //args('check', '--missing', '--settings', '--report', 'proofManagementReport.html', '--replay', '--dependency', 'pmexample2')
    //args('merge', 'bundle1', 'bundle2.zip', 'output.zproof')

    // not necessary any more with the workaround in HTMLReport
    // needed for access of Node.getValue in string template
    // jvmArgs += ['--add-opens', 'java.base/java.util=ALL-UNNAMED']
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier = "exe"
    archiveBaseName = "keyext.proofmanagement"
}