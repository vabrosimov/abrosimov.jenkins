package ru.abrosimov.jenkins.context

interface Application {
    String digest
    final String mavenGroup
    final String mavenArtifact
}