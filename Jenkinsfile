pipeline {
    agent any

    stages {
        stage('Run k6 test') {
            steps {
                // Запускаем тест, результат сохраняем в файл
                sh 'k6 run /k6/script.js --summary-export=summary.json'
            }
        }

        stage('Show Summary') {
            steps {
                script {
                    def summary = readJSON file: 'summary.json'
                    echo "🧪 Запросов всего: ${summary.metrics.http_reqs.count}"
                    echo "⏱ Средняя задержка: ${summary.metrics.http_req_duration.avg} мс"
                    echo "✅ Успешных: ${summary.metrics.checks.passes}"
                    echo "❌ Ошибок: ${summary.metrics.checks.fails}"
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
