pipeline {
  agent {
    docker {
      image 'localhost/maven-docker:jdk11'
      args '-v $HOME/.m2:/root/.m2 -m 1G -v /var/run/docker.sock:/var/run/docker.sock -u 0:0 --group-add docker -e MAVEN_OPTS="-Xmx300m"'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn -B versions:set -DnewVersion=${BUILD_NUMBER}'
        sh 'mvn -B clean install'
      }
    }
    stage('Build images') {
      steps {
        sh 'mvn -B dockerfile:build -Pcreate-image'
      }
    }
  }
}
