cd frontend && \
export SERVER_URL="http://localhost:8080/" && \
yarn && \
yarn build && \
cd .. && \
rm -rf api/src/main/resources/static/\* && \
cp -a frontend/www/ api/src/main/resources/static;
