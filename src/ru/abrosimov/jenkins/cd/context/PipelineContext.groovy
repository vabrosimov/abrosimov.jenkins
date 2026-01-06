package ru.abrosimov.jenkins.cd.context

import ru.abrosimov.jenkins.core.Jenkins

abstract class PipelineContext extends Jenkins {

    PipelineContext(Object jenkins) {
        super(jenkins)
    }

    abstract String getRegistry();

    abstract List<Application> getApplications();
    void setApplications(List<Application> applications) {
        this.applications = applications
    }
}