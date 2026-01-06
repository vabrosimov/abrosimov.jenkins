package ru.abrosimov.jenkins.ci.stages

import ru.abrosimov.jenkins.ci.context.PipelineContext
import ru.abrosimov.jenkins.core.Jenkins

class BuildDockerImage extends Jenkins {

    BuildDockerImage(Object jenkins) {
        super(jenkins)
    }

    void call(PipelineContext pipelineContext) {
        jenkins.sh """
                       docker build \
                       --platform=linux/amd64 \
                       -t ${pipelineContext.registry}${pipelineContext.application.image}:${pipelineContext.application.version} \
                       -t ${pipelineContext.registry}${pipelineContext.application.image}:latest \
                       .
                   """
    }
}