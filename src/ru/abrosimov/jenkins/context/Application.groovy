package ru.abrosimov.jenkins.context

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