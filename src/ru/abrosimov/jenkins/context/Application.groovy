package ru.abrosimov.jenkins.context

interface Application {
    final String mavenGroup
    final String mavenArtifact
    final String image
    final String versionParamName
    String digest
    String version
}