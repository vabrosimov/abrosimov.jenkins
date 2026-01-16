package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.ci.context.PipelineContext
import ru.abrosimov.jenkins.core.Jenkins

class PushToRegistry extends Jenkins {

    PushToRegistry(Object jenkins) {
        super(jenkins)
    }

    void call(PipelineContext pipelineContext) {
        GString imageWithVersion = "${pipelineContext.registry}${pipelineContext.application.image}:${pipelineContext.application.version}"
        GString imageLatest = "${pipelineContext.registry}${pipelineContext.application.image}:latest"

        jenkins.withCredentials([
                jenkins.usernamePassword(
                        credentialsId: "NEXUS_CREDENTIALS",
                        usernameVariable: "NEXUS_USER",
                        passwordVariable: "NEXUS_PASSWORD"
                )
        ]) {
            jenkins.withEnv([
                    "IMAGE_WITH_VERSION=${imageWithVersion}",
                    "IMAGE_LATEST=${imageLatest}",
                    "REGISTRY=${pipelineContext.registry}"
            ]) {
                jenkins.sh '''
                               echo "$NEXUS_PASSWORD" | sudo docker login "$REGISTRY" \
                                -u "$NEXUS_USER" \
                                --password-stdin

                               docker push "$IMAGE_WITH_VERSION"
                               docker push "$IMAGE_LATEST"
                            '''
            }
        }
    }
}