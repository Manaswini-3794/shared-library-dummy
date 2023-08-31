def checkoutRepo(repoMap){

    for(it in repoMap){
        
        def repourl = "ssh://" + "${USER}" + "@www.collabnet.nxp.com:29418/" + "${it.repo}"
        checkout([$class: 'GitSCM', branches: [[name: it.branch]],
        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: it.repo ]],
        userRemoteConfigs: [[credentialsId: CRED_ID, url: repourl]]])

        dir("${it.repo}@temp"){
            deleteDir()
        }
    }
   
}

return this

    
