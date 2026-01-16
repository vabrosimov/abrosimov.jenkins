package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.ci.context.PipelineContext
import ru.abrosimov.jenkins.core.Jenkins

class Checkout extends Jenkins {

    Checkout(Object jenkins) {
        super(jenkins)
    }

    void call(PipelineContext pipelineContext) {
        jenkins.sshagent(["SSH_KEY_GITHUB"]) {
            jenkins.git(
                    url: "${pipelineContext.application.git}",
                    branch: "${pipelineContext.application.gitBranch}",
                    credentialsId: "SSH_KEY_GITHUB"
            )
        }
    }
}