pipeline {
    agent any

    stages {
        stage('Run Docker') {
            steps {
                script {
                    // Запускаем контейнер, который просто печатает слово
                    sh 'docker run --rm alpine echo "Привет из Docker!"'
                }
            }
        }
    }
}
