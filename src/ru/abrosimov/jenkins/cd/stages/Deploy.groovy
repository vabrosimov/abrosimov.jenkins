package ru.abrosimov.jenkins.cd.stages

import ru.abrosimov.jenkins.cd.context.Application
import ru.abrosimov.jenkins.core.Jenkins

class Deploy extends Jenkins {

    Deploy(Object jenkins) {
        super(jenkins)
    }

    void call(Application application) {
        String version = jenkins.params[application.versionParamName]

        if (version == "SKIP_INSTALL") {
            return
        }

        jenkins.sshagent(['SSH_KEY_VM']) {
            jenkins.sh """
                mkdir -p -m 700 ~/.ssh
                ssh-keyscan -H ${application.vmAddress} >> ~/.ssh/known_hosts
                chmod 600 ~/.ssh/known_hosts
                
                ssh -o StrictHostKeyChecking=no ${application.vmUser}@${application.vmAddress} \\
                "sudo mkdir -p -m 755 /opt/${application.mavenArtifact} && sudo chown ${application.vmUser}:${application.vmUser} /opt/${application.mavenArtifact} && sudo chmod 755 /opt/${application.mavenArtifact}"
                
                scp src/apps/${application.mavenArtifact}/docker-compose.yml ${application.vmUser}@${application.vmAddress}:/opt/${application.mavenArtifact}/docker-compose.yml
                
                ssh -o StrictHostKeyChecking=no ${application.vmUser}@${application.vmAddress} \\
                "cd /opt/${application.mavenArtifact} && sudo docker compose up -d --force-recreate"
            """
        }
    }
}