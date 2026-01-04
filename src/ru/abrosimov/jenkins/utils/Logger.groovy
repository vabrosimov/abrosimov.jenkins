package ru.abrosimov.jenkins.utils

import ru.abrosimov.jenkins.core.Jenkins

class Logger extends Jenkins {

    Logger(Object jenkins) {
        super(jenkins)
    }

    def logStartStage() {
        jenkins.ansiColor('xterm') {
            jenkins.echo """
        \u001B[34m══════════════════════════════════════════════\u001B[0m
        \u001B[36m▶▶▶ START STAGE: ${jenkins.STAGE_NAME}\u001B[0m
        \u001B[34m══════════════════════════════════════════════\u001B[0m
        """.stripIndent()
        }
    }

    def logEndStage() {
        jenkins.ansiColor('xterm') {
            jenkins.echo """
        \u001B[32m✔✔✔ END STAGE: ${jenkins.STAGE_NAME}\u001B[0m
        """.stripIndent()
        }
    }
}