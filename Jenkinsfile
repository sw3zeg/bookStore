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

        stage('Wait for API to be ready') {
            steps {
                script {
                    sh '''
                    echo "⏳ Ждём старта API на http://localhost:8080/api/books..."
                    for i in {1..30}; do
                      if curl -s http://localhost:8080/api/books > /dev/null; then
                        echo "✅ API готов!"
                        exit 0
                      fi
                      sleep 1
                    done
                    echo "❌ API не ответил за 30 секунд"
                    exit 1
                    '''
                }
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
        script {
            def summary = readJSON file: 'summary.json'
            def totalReqs = summary.metrics.http_reqs.count
            def successChecks = summary.metrics.checks.passes
            def failedChecks = summary.metrics.checks.fails
            def avgLatency = summary.metrics.http_req_duration.avg
            def latency95 = summary.metrics.http_req_duration['p(95)']
            
            def msg = """
                📊 *Результаты k6-тестирования*:
                🧪 Запросов: *${totalReqs}*
                ✅ Успешных чеков: *${successChecks}*
                ❌ Проваленных чеков: *${failedChecks}*
                ⏱ Средняя задержка: *${avgLatency} мс*
                📈 95-й процентиль: *${latency95} мс*
                """

            def BOT_TOKEN = '8060387975:AAGTxAHHqHZo7LKpD4z7aLKx7LEZSngh8k8'
            def CHAT_ID = '958007638'

            sh """
            curl -s -X POST "https://api.telegram.org/bot${BOT_TOKEN}/sendMessage" \\
              -d chat_id=${CHAT_ID} \\
              -d parse_mode=Markdown \\
              -d text="\$(echo '${msg}' | sed 's/"/\\"/g')"
            """
        }
    }
}

    post1 {
        always {
            archiveArtifacts artifacts: 'summary.json', fingerprint: true
        }
    }
}
