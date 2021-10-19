echo 'JAR-ING' && \
cd api && \
./gradlew bootjar && \
cd .. && \
cp api/build/libs/cordtables-1.jar infra/local-peer/cordtables.jar; # && \

# echo 'CONTAINERIZING' && \
# cd infra/dev/cf-server && \
# docker build -f Dockerfile -t cf-server:latest . --progress plain && \

# java -jar infra/local-peer/cordtables.jar --spring.config.location=infra/local-peer/application.yml