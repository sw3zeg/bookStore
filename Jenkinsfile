pipeline {
    agent any

    stages {
        stage('Run k6 test') {
            steps {
                sh '''
                    docker run --rm \
                    -v ./k6:/k6 \
                    grafana/k6 run /k6/script.js
                '''
            }
        }

        stage('Show Summary') {
            steps {
                script {
                    def summary = readJSON file: 'k6/summary.json'
                    echo "🧪 Запросов: ${summary.metrics.http_reqs.count}"
                    echo "✅ Успешных: ${summary.metrics.checks.passes}"
                    echo "❌ Ошибок: ${summary.metrics.checks.fails}"
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'k6/summary.json', fingerprint: true
        }
    }
}
