pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6' // Configurer dans Jenkins Global Tool Configuration
    }
    
    environment {
        SLACK_CHANNEL = '#devops-notifications' // Votre canal Slack
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code depuis GitHub...'
                git branch: 'main',
                    url: 'https://github.com/VotreUsername/Projet-DevOps-VotreNomPrenom.git'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compilation du projet avec Maven...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Exécution des tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Création du package JAR...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Archive') {
            steps {
                echo 'Archivage des artifacts...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                echo 'Déploiement de l\'application...'
                sh 'java -jar target/*.jar'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline exécuté avec succès!'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: "✅ Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}\nBranche: ${env.GIT_BRANCH}\n${env.BUILD_URL}"
            )
        }
        failure {
            echo 'Le pipeline a échoué.'
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: "❌ Build FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}\nBranche: ${env.GIT_BRANCH}\n${env.BUILD_URL}"
            )
        }
    }
}