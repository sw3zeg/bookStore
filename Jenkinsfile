pipeline {
    agent any
    stages {
        stage('Run Docker container') {
            steps {
                script {
                    sh 'docker run hello-world'
                }
            }
        }
    }
}
