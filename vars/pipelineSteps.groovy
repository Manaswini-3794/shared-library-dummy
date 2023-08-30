def pipeLine(){

    pipeline {
        agent {
            node { 
                label 'lsv03260-Prod'
            } 
        }

        options {
            // time stamps to console output
            timestamps()
            buildDiscarder(logRotator(numToKeepStr: '10'))
        }

        stages {
            stage('cleaning workspace before build') {
                steps {
                    // Cleaning workspace before build starts
                    cleanWs() 
                }
            }

            stage('Reading Environmental variables from Properties file'){
                steps{
                    //loading roperties file
                    load 'path/to/file.properties'
                }
            }

            stage('checkout from repo'){
                steps{
                    script{
                        def repo_list = loadRepo.readRepoList()
                        scmCheckout.checkoutRepo(repo_list)
                    }
                }
            }
    
            stage('Shell script') {
                steps {
                    sh '''rm -rf $ANDROID_ROOT_DIR/out/.lock*

                        cd /opt/samba/nxf24591/fireworks/nfcandroid_setup
                        git reset --hard origin/master
                        git checkout master
                        git pull
                        #git pull "ssh://nxf24591@www.collabnet.nxp.com:29418/nfcandroid_setup" refs/changes/08/146608/41
                        patch -p1 < No_Verify.patch

                        export PROJECT_ENV=SNxxx_14
                        export PATH=$(pwd):$PATH
                        export PROJECT_ROOT_ENV=/opt/samba/nxf24591/hikey_sources/ar_14_master_19_05_23_gen_snxxx
                        sudo rm -rf $PROJECT_ROOT_ENV/middleware_ws*
                        trigger.sh

                        cd $WORKSPACE/build_image
                        ZIP_NAME=$(ls *.zip)
                        REPOSITORY_ID=NFC-AR-MW-Snapshot
                        GROUP_ID=AR14/SNxxx
                        ARTIFACT_ID=GEN/Hikey960/$BUILD_NUMBER
                        PACKAGE=zip	
                        curl --fail -u nxf42069:Automation2022 --upload-file $ZIP_NAME "https://in-nxrm.sw.nxp.com/repository/$REPOSITORY_ID/$GROUP_ID/$ARTIFACT_ID/"

                        NEXUS_URL=https://in-nxrm.sw.nxp.com/repository/$REPOSITORY_ID/$GROUP_ID/$ARTIFACT_ID/$ZIP_NAME
                        echo $NEXUS_URL >> $WORKSPACE/mw.properties'''
                }
            }
            stage('sending Emails'){
                steps{
                    script{
                        emailext body: '$DEFAULT_CONTENT', replyTo: '$DEFAULT_REPLYTO',
                        subject: '$DEFAULT_SUBJECT', to: '$DEFAULT_RECIPIENTS'
                    }
                }
            }
            stage('Send log to logstash'){
			    steps{
	                logstashSend failBuild: false, maxLines: -1
          		    }
			}

        }
        post {
            always {
                step($class: 'ArtifactArchiver', 
                artifacts: 'testbench/Android_TestSuite/**,mw.properties,build_image/static_memory.log,build_image/mw_warning_count,build_image/static_memory_count,build_image/mw_warnings.log',
                followSymlinks: false)
            }

        }
  
    }
}