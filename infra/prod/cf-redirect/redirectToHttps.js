var http = require("http");
console.log("Redirecting from http to https. Listening on port 8080");
http
  .createServer(function (req, res) {
    res.writeHead(301, { Location: "https://cordtables.com" + req.url });
    res.end();
  })
  .listen(8080);
