package ru.abrosimov.jenkins.core

class Logger {

    static void startStage(Object jenkins) {
        jenkins.ansiColor('xterm') {
            jenkins.echo """
        \u001B[34m══════════════════════════════════════════════\u001B[0m
        \u001B[36m▶▶▶ START STAGE: ${jenkins.STAGE_NAME}\u001B[0m
        \u001B[34m══════════════════════════════════════════════\u001B[0m
        """.stripIndent()
        }
    }

    static void endStage(Object jenkins) {
        jenkins.ansiColor('xterm') {
            jenkins.echo """
        \u001B[32m✔✔✔ END STAGE: ${jenkins.STAGE_NAME}\u001B[0m
        """.stripIndent()
        }
    }
}