#!/bin/bash
if [ $1 > 0 ]
then

    # BUILD
    cd ../../frontend && \
    npm run build && \
    cd /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2 && \
    rm -rf /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/src/main/resources/static/* && \
    cp -r /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/frontend/dist/frontend/* /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/src/main/resources/static && \
    mvn package -Dmaven.test.skip=true && \
    cp /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/target/api.jar /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/docker/cas2-server && \

    # CONTAINERIZE
    cd /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/docker/cas2-server && \
    docker build -f /mnt/c/Users/Michael/CrowdAltar/Projects/caserver2/docker/cas2-server/Dockerfile -t cas2-server:$1 -t cas2-server:latest . --progress plain && \

    # PUSH
    export AWS_PROFILE=michaeladmin && \
    $(aws ecr get-login --no-include-email --region us-east-2) && \
    docker tag cas2-server:$1 522113699904.dkr.ecr.us-east-2.amazonaws.com/cas2-server:$1 && \
    docker tag cas2-server:latest 522113699904.dkr.ecr.us-east-2.amazonaws.com/cas2-server:latest && \
    docker push 522113699904.dkr.ecr.us-east-2.amazonaws.com/cas2-server:$1 && \
    docker push 522113699904.dkr.ecr.us-east-2.amazonaws.com/cas2-server:latest 

else 
    echo 'please run again with a version number';
fi