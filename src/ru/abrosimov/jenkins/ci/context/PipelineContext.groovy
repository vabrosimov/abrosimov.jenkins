package ru.abrosimov.jenkins.ci.context

import ru.abrosimov.jenkins.core.Jenkins

abstract class PipelineContext extends Jenkins {
    abstract String getRegistry();

    abstract Application getApplication();
    void setApplication(Application application) {
        this.application = application
    }

    PipelineContext(Object jenkins) {
        super(jenkins)
    }
}