pipeline {
    agent any

    stages {
        stage('Run k6 test') {
            steps {
                sh '''
                    docker run --rm \
                    -v $(pwd)/k6:/scripts \
                    -w /scripts \
                    grafana/k6 run script.js --summary-export=summary.json
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
