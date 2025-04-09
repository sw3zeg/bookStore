pipeline {
    agent any

    stages {
        stage('Run k6 test') {
            steps {
                sh 'k6 run ./k6/script.js --summary-export=summary.json'
            }
        }

        stage('Show Summary') {
            steps {
                script {
                    def summary = readJSON file: 'summary.json'
                    
                    echo "🧪 Запросов всего: ${summary.metrics.http_reqs.count}"
                    echo "✅ Успешных чеков: ${summary.metrics.checks.passes}"
                    echo "❌ Проваленных чеков: ${summary.metrics.checks.fails}"
                    echo "⏱ Средняя задержка: ${summary.metrics.http_req_duration.avg} мс"
                    echo "📈 95-й процентиль задержки: ${summary.metrics.http_req_duration['p(95)']} мс"
                    echo "📥 Получено данных: ${summary.metrics.data_received.total / 1024 / 1024} MB"
                    echo "📤 Отправлено данных: ${summary.metrics.data_sent.total / 1024 / 1024} MB"
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'summary.json', fingerprint: true
        }
    }
}
