package ru.abrosimov.jenkins.cd.context

interface Application {
    String getMavenGroup()
    String getMavenArtifact()
    String getImage()
    String getVersionParamName()
    String getVmAddress()
    String getVmUser()

    String getDigest()
    void setDigest(String digest)
}