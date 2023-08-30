def checkoutRepo(repoMap){
    
    //def readFromRepo = evaluate(new File('loadRepo.groovy'))
    //def repos = readFromRepo.readRepoList()

    for(it in repoMap){
        //println elem.repo
        //println elem.branch
        def repourl = "ssh://" + "${USER}" + "@www.collabnet.nxp.com:29418/" + "${it.repo}"
        checkout([$class: 'GitSCM', branches: [[name: "*/${it.branch}"]],
        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: it.repo ]],
        userRemoteConfigs: [[credentialsId: CREDENTIALS, url: repourl]]])

        def folder = new File("${it.repo}@tmp")
        folder.deleteDir()    
    }
   
}

return this

    
