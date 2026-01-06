package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.core.Jenkins

class BuildAndPublish extends Jenkins {

    BuildAndPublish(Object jenkins) {
        super(jenkins)
    }

    void call() {
        jenkins.withCredentials([
                jenkins.usernamePassword(
                        credentialsId: "NEXUS_CREDENTIALS",
                        usernameVariable: "NEXUS_USER",
                        passwordVariable: "NEXUS_PASSWORD"
                )
        ]) {
            jenkins.sh '''
                           ./gradlew clean publish -PNEXUS_USER=$NEXUS_USER -PNEXUS_PASSWORD=$NEXUS_PASSWORD
                       '''
        }
    }
}