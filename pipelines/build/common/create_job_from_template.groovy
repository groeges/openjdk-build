/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/**
 * A template that defines a build job.
 *
 * This mostly is just a wrapper to call the openjdk_build_pipeline.groovy script that defines the majority of
 * what a pipeline job does
 */

String buildFolder = "$JOB_FOLDER"

if (!binding.hasVariable('JDK_BOOT_VERSION')) JDK_BOOT_VERSION = ""
if (!binding.hasVariable('CONFIGURE_ARGS')) CONFIGURE_ARGS = ""
if (!binding.hasVariable('BUILD_ARGS')) BUILD_ARGS = ""
if (!binding.hasVariable('ADDITIONAL_FILE_NAME_TAG')) ADDITIONAL_FILE_NAME_TAG = ""
if (!binding.hasVariable('TEST_LIST')) TEST_LIST = ""
if (!binding.hasVariable('ENABLE_TESTS')) ENABLE_TESTS = false
if (!binding.hasVariable('SCM_REF')) SCM_REF = ""
if (!binding.hasVariable('CLEAN_WORKSPACE')) CLEAN_WORKSPACE = false
if (!binding.hasVariable('RELEASE')) RELEASE = false
if (!binding.hasVariable('OVERRIDE_FILE_NAME_VERSION')) OVERRIDE_FILE_NAME_VERSION = ""
if (!binding.hasVariable('PUBLISH_NAME')) PUBLISH_NAME = ""
if (!binding.hasVariable('ADOPT_BUILD_NUMBER')) ADOPT_BUILD_NUMBER = ""


if (!binding.hasVariable('GIT_URI')) GIT_URI = "https://github.com/AdoptOpenJDK/openjdk-build.git"
if (!binding.hasVariable('GIT_BRANCH')) GIT_BRANCH = "new_build_scripts"


folder(buildFolder) {
    description 'Automatically generated build jobs.'
}


pipelineJob("$buildFolder/$JOB_NAME") {
    description('<h1>THIS IS AN AUTOMATICALLY GENERATED JOB DO NOT MODIFY, IT WILL BE OVERWRITTEN.</h1><p>This job is defined in create_job_from_template.groovy in the openjdk-build repo, if you wish to change it modify that</p>')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(GIT_URI)
                        refspec(" +refs/pull/*:refs/remotes/origin/pr/* +refs/heads/master:refs/remotes/origin/master +refs/heads/*:refs/remotes/origin/*")
                    }
                    branch("${GIT_BRANCH}")
                    extensions {
                        cleanBeforeCheckout()
                        pruneStaleBranch()
                    }
                }
            }
            scriptPath('pipelines/build/common/openjdk_build_pipeline.groovy')
        }
    }
    properties {
        copyArtifactPermissionProperty {
            projectNames('*')
        }
    }
    logRotator {
        numToKeep(5)
    }

    parameters {
        stringParam('SCM_REF', null, "git tag/branch/commit to build if not HEAD")
        stringParam('NODE_LABEL', "$NODE_LABEL")
        stringParam('JAVA_TO_BUILD', "$JAVA_TO_BUILD")
        stringParam('JDK_BOOT_VERSION', "${JDK_BOOT_VERSION}")
        stringParam('CONFIGURE_ARGS', "$CONFIGURE_ARGS", "Additional arguments to pass to ./configure")
        stringParam('BUILD_ARGS', "$BUILD_ARGS", "additional args to call makejdk-any-platform.sh with")
        stringParam('ARCHITECTURE', "$ARCHITECTURE")
        stringParam('VARIANT', "$VARIANT")
        stringParam('TARGET_OS', "$TARGET_OS")
        stringParam('ADDITIONAL_FILE_NAME_TAG', "$ADDITIONAL_FILE_NAME_TAG")
        stringParam('OVERRIDE_FILE_NAME_VERSION', "$OVERRIDE_FILE_NAME_VERSION")
        booleanParam('ENABLE_TESTS', ENABLE_TESTS)
        booleanParam('CLEAN_WORKSPACE', CLEAN_WORKSPACE)
        booleanParam('RELEASE', RELEASE)
        stringParam('PUBLISH_NAME', "$PUBLISH_NAME")
        stringParam('ADOPT_BUILD_NUMBER', "$ADOPT_BUILD_NUMBER")
        stringParam('TEST_LIST', "$TEST_LIST")
    }
}