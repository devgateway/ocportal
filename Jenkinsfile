pipeline {
    agent any
    tools {
        maven 'Maven36'
    }

    stages {
        stage('Build') {
            steps {
                 sh 'mvn -T 4C clean package -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -Dmaven.junit.fork=true -DskipTests'
                archiveArtifacts artifacts: 'forms/target/forms*.jar', fingerprint: true
            }
        }
    }
}