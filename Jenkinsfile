pipeline {
    agent any

    stages {
        stage('Run Docker Container') {
            steps {
                script {
                    sh 'docker run hello-world'
                }
            }
        }
    }
}
