package ru.abrosimov.jenkins.ci.context

interface Application {
    String getImage()
    String getGit()
    String getGitBranch()

    String getVersion()
    void setVersion(String version)
}