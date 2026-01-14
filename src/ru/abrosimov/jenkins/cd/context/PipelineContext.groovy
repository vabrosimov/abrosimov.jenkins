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

    private Map<Application, Map> modelByApplication
    Map getModel(Application application) {
        return modelByApplication.get(application)
    }

    void initModelByApplication() {
        applications.each { Application application ->
            Map model = [
                    "mavenGroup": application.mavenGroup,
                    "mavenArtifact": application.mavenArtifact,
                    "image": application.image,
                    "digest": application.digest
            ]

            modelByApplication.put(application, model)
        }
    }
}