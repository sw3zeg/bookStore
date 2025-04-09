pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run k6 test') {
            steps {
                sh '''
                    docker run --rm \
                        -v $(pwd)/k6:/k6 \
                        grafana/k6 run /k6/script.js
                '''
            }
        }
    }
}
