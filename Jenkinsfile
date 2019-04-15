pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-12'
      args '-v $HOME/.m2:/root/.m2 -u 0:0'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn versions:set -DnewVersion=${BUILD_NUMBER}'
        sh 'mvn clean verify -Pcreate-image'
      }
    }
  }
}
