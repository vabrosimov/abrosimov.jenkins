package ru.abrosimov.jenkins.cd.context

import ru.abrosimov.jenkins.core.Jenkins

class PipelineContext extends Jenkins {
    List<Application> applications = []

    PipelineContext(Object jenkins) {
        super(jenkins)
    }
}