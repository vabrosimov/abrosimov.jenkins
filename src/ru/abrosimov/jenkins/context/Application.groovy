package ru.abrosimov.jenkins.context

interface Application {
    String digest
    String mavenGroup
    String mavenArtifact
}