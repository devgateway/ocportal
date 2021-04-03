pipeline {
    agent any
    tools {
        maven 'Maven36'
    }

    stages {
        stage('Build') {
            steps {
                 sh 'mvn -T 2C clean install -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -Dmaven.junit.fork=true -DskipTests'
                archiveArtifacts artifacts: 'forms/target/forms*.jar', fingerprint: true
            }
        }
    }
}