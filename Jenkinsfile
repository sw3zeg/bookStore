pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/sw3zeg/bookStore.git'
            }
        }
        stage('Build') {
            steps {
                sh 'ls -la'
            }
        }
    }
}
