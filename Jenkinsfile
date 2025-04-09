pipeline {
    agent any

    stages {
        stage('Print word from Docker') {
            steps {
                script {
                    docker.image('alpine').inside {
                        sh 'echo "Привет из контейнера!"'
                    }
                }
            }
        }
    }
}
