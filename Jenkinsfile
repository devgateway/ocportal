pipeline {
    agent any
    tools {
        maven 'Maven36'
    }

    stages {
        stage('Build') {
            steps {
            withMaven(mavenOpts: "-Xverify:none -Xmx2500m -XX:+TieredCompilation -XX:TieredStopAtLevel=1") {
                sh 'mvn clean package -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -Dmaven.junit.fork=true -DskipTests'
                }
                archiveArtifacts artifacts: 'forms/target/forms*.jar', fingerprint: true
            }
        }
    }
    post {
            always {
                cleanWs(cleanWhenNotBuilt: false, deleteDirs: true)
            }
        }

}