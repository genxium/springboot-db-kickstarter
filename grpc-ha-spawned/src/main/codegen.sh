#!/bin/bash
basedir=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

DIR_OF_PROTO_FILE=$basedir/proto
PROTO_FILE=helloworld.proto
OUTPUT_FILE=$basedir/java/com/mytrial/pb

if [ -z $PROTOC_BIN_DIR ]; then
   echo "Set the env variable '$PROTOC_BIN_DIR' first!" 
   exit 1
fi

JAVA_GRPC_PLUGIN_PATH=$PROTOC_BIN_DIR/protoc-gen-grpc-java # on Windows, manually delete the ".exe" extension for compatibility
echo $JAVA_GRPC_PLUGIN_PATH 

# Download prebuilt binary of protoc-gen-grpc-java from https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/ and set $PATH.
protoc --plugin=protoc-gen-grpc-java=$JAVA_GRPC_PLUGIN_PATH --grpc-java_out="$OUTPUT_FILE" --java_out="$OUTPUT_FILE" --proto_path="$DIR_OF_PROTO_FILE" "$PROTO_FILE"
