package ru.abrosimov.jenkins.cd.stages

import ru.abrosimov.jenkins.cd.context.Application
import ru.abrosimov.jenkins.core.Jenkins

class ConfigurePipeline extends Jenkins {

    ConfigurePipeline(Object jenkins) {
        super(jenkins)
    }

    List<Object> call(Application application) {
        String repo = "maven-releases"
        GString apiUrl = "${jenkins.REPOSITORY}/service/rest/v1/search?repository=${repo}&group=${application.mavenGroup}&name=${application.mavenArtifact}"

        jenkins.withCredentials([
                jenkins.usernamePassword(
                        credentialsId: "NEXUS_CREDENTIALS",
                        usernameVariable: "NEXUS_USER",
                        passwordVariable: "NEXUS_PASSWORD"
                )
        ]) {
            jenkins.withEnv([
                    "API_URL=${apiUrl}"
            ]) {
                List<String> versions = jenkins.sh(
                        script: '''
                                curl -s -u "$NEXUS_USER:$NEXUS_PASSWORD" \
                                "$API_URL" \
                                | grep '"version"' \
                                | sed 's/.*"version"[ ]*:[ ]*"//' \
                                | sed 's/".*//' \
                                | grep -v SNAPSHOT \
                                | sort -Vr \
                                | uniq
                                ''',
                        returnStdout: true
                ).trim().split("\n")
                versions.add(0, 'SKIP_INSTALL')

                if (versions.isEmpty()) {
                    jenkins.error "No release versions found in Nexus for ${application.mavenGroup}.${application.mavenArtifact}"
                }

                jenkins.echo "Found versions in Nexus for ${application.mavenGroup}.${application.mavenArtifact}: ${versions}"

                return [jenkins.choice(
                        name: application.versionParamName,
                        choices: versions,
                        description: 'Version to deploy'
                )]
            }
        }
    }
}