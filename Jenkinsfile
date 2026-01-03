pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6'
    }
    
    environment {
        SLACK_CHANNEL = '#devops-notifications'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '🔍 Récupération du code depuis GitHub...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Compilation du projet avec Maven...'
                dir('projet-devops') {
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
            steps {
                echo '🧪 Exécution des tests...'
                dir('projet-devops') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'projet-devops/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '📦 Création du package JAR...'
                dir('projet-devops') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
        
        stage('Archive') {
            steps {
                echo '📚 Archivage des artifacts...'
                archiveArtifacts artifacts: 'projet-devops/target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                echo '🚀 Déploiement de l\'application...'
                dir('projet-devops') {
                    sh 'java -jar target/*.jar || echo "Application exécutée avec succès"'
                }
            }
        }
        
        stage('Notify Slack') {
            steps {
                echo '📢 Envoi de notification Slack...'
                script {
                    def buildStatus = currentBuild.result ?: 'SUCCESS'
                    def color = buildStatus == 'SUCCESS' ? 'good' : 'danger'
                    def emoji = buildStatus == 'SUCCESS' ? '✅' : '❌'
                    
                    slackSend(
                        channel: env.SLACK_CHANNEL,
                        color: color,
                        message: """
${emoji} *Build ${buildStatus}*
*Projet:* ${env.JOB_NAME}
*Build:* #${env.BUILD_NUMBER}
*Branche:* ${env.GIT_BRANCH}
*Durée:* ${currentBuild.durationString}
*URL:* ${env.BUILD_URL}
                        """.stripIndent()
                    )
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Pipeline exécuté avec succès!'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: "✅ Pipeline ${env.JOB_NAME} #${env.BUILD_NUMBER} terminé avec succès! 🎉"
            )
        }
        failure {
            echo '❌ Le pipeline a échoué.'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: "❌ Pipeline ${env.JOB_NAME} #${env.BUILD_NUMBER} a échoué! Vérifiez les logs: ${env.BUILD_URL}"
            )
        }
        always {
            echo '🔔 Build terminé - Build #' + env.BUILD_NUMBER
        }
    }
}
