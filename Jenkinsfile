pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/sw3zeg/bookStore.git'
            }
        }
        stage('Echo') {
            steps {
                echo 'Привет!'
            }
        }
    }
}
