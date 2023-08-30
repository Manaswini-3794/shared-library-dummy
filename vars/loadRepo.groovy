def readRepoList(){

   
    def repos = repolist.tokenize(',')
    def brnchs = branchlist.tokenize(',')

    def repoMap = []
    def repo_len = repos.size()

    /* -----Structure of repoMap-----
    repoMap = [
        [repo: repo1, branch: branch1]
        [repo: repo2, branch: branch2]
        [repo: repo3, branch: branch3]
    ]
    */

    for(int i = 0; i < repo_len; i++){
        repoMap << [repo: repos[i], branch: brnchs[i]]
    }
    return repoMap
}

return this