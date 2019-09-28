pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-12'
      args '-v /home/jenkins/.m2:/var/maven/.m2 -e MAVEN_CONFIG=/var/maven/.m2 -e MAVEN_OPTS=-Duser.home=/var/maven'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn versions:set -DnewVersion=${BUILD_NUMBER}'
        sh 'mvn clean install -Pcreate-image'
      }
    }
  }
}
