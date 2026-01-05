package ru.abrosimov.jenkins.stages

import ru.abrosimov.jenkins.context.Application
import ru.abrosimov.jenkins.core.Jenkins

class FindDigest extends Jenkins {

    FindDigest(Object jenkins) {
        super(jenkins)
    }

    void call(Application application) {
        String version = jenkins.params[application.versionParamName]

        if (version == "SKIP_INSTALL") {
            return
        }

        GString manifestsUrl = "${jenkins.REGISTRY}/${application.image}/manifests/${version}"

        jenkins.withCredentials([
                jenkins.usernamePassword(
                        credentialsId: "NEXUS_CREDENTIALS",
                        usernameVariable: "NEXUS_USER",
                        passwordVariable: "NEXUS_PASSWORD"
                )
        ]) {
            jenkins.withEnv([
                    "MANIFESTS_URL=${manifestsUrl}"
            ]) {
                String digest = jenkins.sh(
                        script: '''
                                curl -s -u $NEXUS_USER:$NEXUS_PASSWORD $MANIFESTS_URL |
                                jq -r '.manifests[] | select(.platform.architecture=="amd64") | .digest'
                                ''',
                        returnStdout: true
                ).trim()

                if (digest.isEmpty()) {
                    jenkins.error "No digest found in registry"
                }

                jenkins.echo "Found digest in registry: ${digest}"
                application.setDigest(digest)
            }
        }
    }
}