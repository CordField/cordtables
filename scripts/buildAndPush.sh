echo 'BUILDING' && \
cd frontend && \
# export SERVER_URL="https://cordfield.org/" && \
export SERVER_URL="http://localhost:8080/" && \
yarn && \
yarn build && \
cd .. && \
rm -rf api/src/main/resources/static/* && \
cp -a frontend/www/ api/src/main/resources/static && \
cd api && \
./gradlew test && \
./gradlew bootjar && \
cd .. && \
cp api/build/libs/cordfield-1.jar infra/dev/cf-server/cordfield.jar && \
cp -r api/build/resources/main/static/* infra/dev/cf-server/static && \

echo 'CONTAINERIZING' && \
cd infra/dev/cf-server && \
docker build --no-cache -f Dockerfile -t cf-server:latest . --progress plain && \

echo 'PUSHING' && \
docker tag cf-server:latest 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-server:latest;# && \
# docker push 140251415856.dkr.ecr.us-east-2.amazonaws.com/cf-server:latest;