#!/bin/bash

# This script assumes that you are running maven-3.6.3 or above, please refer to http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html for more information.

if [ $# -ne 2 ]; then   
  echo "Usage: $0 <groupIdSuffix> <artifactId>"   
  exit 1 
fi

basedir=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
groupIdSuffix=$1
groupId=com.mytrial.$groupIdSuffix
archetypeArtifactId=maven-archetype-quickstart
archetypeVersion=1.4

artifactId=$2-spawned
archetypeCatalog=internal

function create_project() {
  if [[ ! -d $basedir/$artifactId ]]; then
    echo "Creating project $artifactId"
    # By default uses offline mode option "-o", remove it if necessary, e.g. in a new environment
    # Add the "-<archetypeCatalog>" for faster catalog search, see https://maven.apache.org/archetype/maven-archetype-plugin/generate-mojo.html#archetypeCatalog for more information.
    cd $basedir && mvn -X -o archetype:generate -DarchetypeCatalog=$archetypeCatalog -DgroupId=$groupId -DartifactId=$artifactId -DarchetypeArtifactId=$archetypeArtifactId -DarchetypeVersion=$archetypeVersion -DinteractiveMode=false
  fi
  cp $basedir/pom-file-templates/artifact-pom.xml $basedir/$artifactId/pom.xml 
  sed -i "s/TO_BE_REPLACED_GROUP_ID_SUFFIX/$groupIdSuffix/g" $basedir/$artifactId/pom.xml
  sed -i "s/TO_BE_REPLACED/$artifactId/g" $basedir/$artifactId/pom.xml
}

create_project
