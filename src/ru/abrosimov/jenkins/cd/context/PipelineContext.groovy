package ru.abrosimov.jenkins.cd.context

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

    abstract List<Application> getApplications();
    void setApplications(List<Application> applications) {
        this.applications = applications
    }

    protected Map<Application, Map> modelByApplication = [:]
    Map getModel(Application application) {
        if (modelByApplication.isEmpty()) {
            initModelByApplication()
        }

        return modelByApplication.get(application)
    }

    protected void initModelByApplication() {
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