From node

EXPOSE 8080

RUN mkdir -p /home/user1/node

ADD ./redirectToHttps.js /home/user1/redirectToHttps.js

ENTRYPOINT node ./home/user1/redirectToHttps.js

RUN echo 'v1'