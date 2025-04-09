pipeline {
    agent any

    stages {
        
        stage('Build BookStore App') {
            steps {
                sh 'docker build -t bookstore-app .'
            }
        }

        stage('Run BookStore API') {
            steps {
                sh 'docker-compose up -d --build'
            }
        }
        
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

                    def dataReceivedBytes = summary.metrics.data_received?.total ?: 0
                    def dataSentBytes     = summary.metrics.data_sent?.total ?: 0
                    echo "📥 Получено данных: ${String.format('%.2f', dataReceivedBytes / 1024 / 1024)} MB"
                    echo "📤 Отправлено данных: ${String.format('%.2f', dataSentBytes / 1024 / 1024)} MB"
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
