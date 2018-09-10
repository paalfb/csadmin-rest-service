
node {

    def mvnHome = '/Users/palborjeson/utvikling/apache-maven-3.5.4'
    def releaseVersion

    stage('Prepare') {
        cleanWs()
    }

    stage('Checkout') {
        sh 'mkdir tmp_build'
        sh 'cd tmp_build'
        sh 'git init'
        withCredentials([string(credentialsId: 'cs-token', variable: 'CS_TOKEN')]) {
            sh 'git pull https://{CS_TOKEN}@github.com/paalfb/csadmin-rest-service.git'
        }
    }
    try {
        stage('Test') {
            sh 'mvn test'
        }
    } catch(Exception e) {
        echo 'what a bummer'
    }
    stage('Package') {
        pom = readMavenPom file: 'pom.xml'
        releaseVersion = pom.version.split("-")[0]
        sh "'${mvnHome}/bin/mvn' --batch-mode release:prepare"
        sh "'${mvnHome}/bin/mvn' --batch-mode release:perform"
        echo releaseVersion

    }
      stage('docker build') {
           withCredentials([usernamePassword(credentialsId: 'docker_uploader', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
               sh 'echo ${env.NEXUS_USERNAME}'
               sh "docker login -u ${env.NEXUS_USERNAME} -p ${env.NEXUS_PASSWORD} pals-mbp.adeo.no:8444"
               sh "docker build -t pals-mbp.adeo.no:8444/csadmin-rest-service:'${releaseVersion}' ."
               sh "docker push pals-mbp.adeo.no:8444/csadmin-rest-service:'${releaseVersion}'"
           }
       }


}