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
            archiveArtifacts artifacts: 'summary.json', fingerprint: true
        }
    }
}
