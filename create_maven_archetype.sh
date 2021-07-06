#!/bin/bash

# This script assumes that you are running maven-3.6.3 or above, please refer to http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html for more information.

if [ $# -ne 2 ]; then   
  echo "Usage: $0 <groupIdSuffix> <artifactId>"   
  exit 1 
fi

basedir=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

groupId=com.mytrial.$groupIdSuffix
archetypeArtifactId=maven-archetype-quickstart
archetypeVersion=1.4

artifactId=$1-spawned

function create_project() {
  if [[ ! -d $basedir/$artifactId ]]; then
    echo "Creating project $artifactId"
    # By default uses offline mode option "-o"
    cd $basedir && mvn -X -o archetype:generate -DgroupId=$groupId -DartifactId=$artifactId -DarchetypeArtifactId=$archetypeArtifactId -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
  fi
  cp $basedir/pom-file-templates/artifact-pom.xml $basedir/$artifactId/pom.xml 
  sed -i "s/TO_BE_REPLACED/$artifactId/g" $basedir/$artifactId/pom.xml
}

create_project
