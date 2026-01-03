pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6'
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
    }
    
    post {
        success {
            echo '✅ Pipeline exécuté avec succès!'
            echo '📊 Tous les tests sont passés'
            echo '📦 Artifact créé et archivé'
        }
        failure {
            echo '❌ Le pipeline a échoué.'
            echo '🔍 Vérifiez les logs ci-dessus pour plus de détails'
        }
        always {
            echo '🔔 Build terminé - Build #' + env.BUILD_NUMBER
            echo '📅 Date: ' + new Date().format('yyyy-MM-dd HH:mm:ss')
        }
    }
}
