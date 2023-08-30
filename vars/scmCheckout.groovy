def checkoutRepo(repoMap){
    
    //def readFromRepo = evaluate(new File('loadRepo.groovy'))
    //def repos = readFromRepo.readRepoList()

    def repos = repoMap.repos
    def branches = repoMap.branches
    //def creds = repoMap.creds
    //def user = repoMap.user

    for(elem in repos){
        println elem
        def repourl = "ssh://" + "${USER}" + "@www.collabnet.nxp.com:29418/" + "${it}"
        checkout([$class: 'GitSCM', branches: [[name: '*/master']],
        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: it ]],
        userRemoteConfigs: [[credentialsId: CREDENTIALS, url: repourl]]])

        def folder = new File("${it}@tmp")
        folder.deleteDir()    
    }
   
}

return this

    
