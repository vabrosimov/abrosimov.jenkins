package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.ci.context.PipelineContext
import ru.abrosimov.jenkins.core.Jenkins

class IncrementVersion extends Jenkins {

    IncrementVersion(Object jenkins) {
        super(jenkins)
    }

    void call(PipelineContext pipelineContext) {
        String versionFile = "version.properties"

        List<String> parts = pipelineContext.application.version.tokenize('-')
        GString nextVersion = "${parts[0]}-${parts[1].toInteger() + 1}"

        jenkins.writeFile(
                file: versionFile,
                text: "version=${nextVersion}\n"
        )

        jenkins.echo "Next version set to: ${nextVersion}"

        jenkins.sshagent(["SSH_KEY_GITHUB"]) {
            jenkins.sh """
                           mkdir -p -m 700 ~/.ssh
                           ssh-keyscan -H github.com >> ~/.ssh/known_hosts
                           chmod 600 ~/.ssh/known_hosts
                            
                           git config user.name "Jenkins CI"
                           git config user.email "ci@jenkins.local"
                           git add ${versionFile}
                           git commit -m "chore: bump version to ${nextVersion}"
                           git push origin master
                       """
        }
    }
}