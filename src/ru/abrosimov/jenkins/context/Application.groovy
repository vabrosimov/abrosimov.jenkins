package ru.abrosimov.jenkins.context

interface Application {
    final String mavenGroup
    final String mavenArtifact
    String digest
    String versionParamName
    String version
}