pipeline {
    agent any

    stages {
        stage('List contents') {
            steps {
                sh '''
                    echo "📂 Содержимое папки ./k6 на хосте:"
                    ls -la ./k6

                    echo "📂 Содержимое /k6 внутри контейнера:"
                    docker run --rm -v $(pwd)/k6:/k6 alpine ls -la /k6
                '''
            }
        }
}
