package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.core.Jenkins

class GetVersion extends Jenkins {

    GetVersion(Object jenkins) {
        super(jenkins)
    }

    String call() {
        String versionFile = "version.properties"

        String currentVersion = jenkins.sh(
                script: "grep '^version=' ${versionFile} | cut -d'=' -f2",
                returnStdout: true
        ).trim()
        jenkins.echo "Current version: ${currentVersion}"

        return currentVersion
    }
}