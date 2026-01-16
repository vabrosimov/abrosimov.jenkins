package ru.abrosimov.jenkins.cd.stages

import ru.abrosimov.jenkins.cd.context.Application
import ru.abrosimov.jenkins.cd.context.PipelineContext
import ru.abrosimov.jenkins.core.Jenkins
import ru.abrosimov.jenkins.core.TemplateProcessor

class Deploy extends Jenkins {

    Deploy(Object jenkins) {
        super(jenkins)
    }

    void call(PipelineContext pipelineContext, Application application) {
        String version = jenkins.params[application.versionParamName]

        if (version == "SKIP_INSTALL") {
            return
        }

        TemplateProcessor.process(
                jenkins,
                "src/apps/${application.mavenArtifact}/docker-compose.yml",
                pipelineContext.getModel(application),
                "docker-compose.yml.processed"
        )

        jenkins.withCredentials([
                jenkins.usernamePassword(credentialsId: "NEXUS_CREDENTIALS",
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD')
        ]) {
            jenkins.withEnv([
                    "VM_USER=${application.vmUser}",
                    "VM_ADDRESS=${application.vmAddress}",
                    "REGISTRY=${pipelineContext.registry}"
            ]) {
                jenkins.sshagent(['SSH_KEY_VM']) {
                    jenkins.sh '''
                        ssh $VM_USER@$VM_ADDRESS "
                            echo $NEXUS_PASSWORD | sudo docker login "$REGISTRY" \
                            -u $NEXUS_USER \
                            --password-stdin
                        "
                    '''
                }
            }
        }

        jenkins.sshagent(['SSH_KEY_VM']) {
            jenkins.sh """
                mkdir -p -m 700 ~/.ssh
                ssh-keyscan -H ${application.vmAddress} >> ~/.ssh/known_hosts
                chmod 600 ~/.ssh/known_hosts
                
                ssh -o StrictHostKeyChecking=no ${application.vmUser}@${application.vmAddress} \\
                "sudo mkdir -p -m 755 /opt/${application.mavenArtifact} && sudo chown ${application.vmUser}:${application.vmUser} /opt/${application.mavenArtifact} && sudo chmod 755 /opt/${application.mavenArtifact}"
                
                scp docker-compose.yml.processed ${application.vmUser}@${application.vmAddress}:/opt/${application.mavenArtifact}/docker-compose.yml
                
                ssh -o StrictHostKeyChecking=no ${application.vmUser}@${application.vmAddress} \\
                "cd /opt/${application.mavenArtifact} && sudo docker compose up -d --force-recreate"
            """
        }
    }
}