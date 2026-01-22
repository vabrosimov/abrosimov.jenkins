package ru.abrosimov.jenkins.ci.context

import ru.abrosimov.jenkins.core.Jenkins

abstract class PipelineContext extends Jenkins {

    PipelineContext(Object jenkins) {
        super(jenkins)
    }

    void appendBuildDescription(String line) {
        String description = jenkins.currentBuild.description ?: ""
        description += line.endsWith("\n") ? line : line + "\n"
        jenkins.currentBuild.description = description
    }

    abstract String getRegistry();

    abstract Application getApplication();
    void setApplication(Application application) {
        this.application = application
    }
}