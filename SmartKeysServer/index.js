var FCM = require("fcm-node");
var url = require("url");
var querystring = require("querystring");
const express = require("express");
const app = express();

var serverKey =
  "AAAANGXQVl0:APA91bHCDw4tQ5npab6DKa3IbTsrUQJLc2rClm4iiSGZkwBEVJMBpzx4GHN0vhFSKX_ct82c9kQESzUwxPEg5ysuT07gDdiNI3AJllhr_qPlZl5G8vnkEOGSL1WeGWThG1CWqVlHfVry";
var fcm = new FCM(serverKey);

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  next();
});

app.get("/", (req, res) => {
  var params = querystring.parse(url.parse(req.url).query);
  if ("token" in params && "notificationBody" in params) {
    const token = params["token"];
    const notificationBody = params["notificationBody"];
    var message = {
      to: token,
      notification: {
        title: "Title of your push notification",
        body: notificationBody
      }
    };
    fcm.send(message, function(err, response) {
      res.send("ok");
    });
  } else {
    res.send("failed");
  }
});

app.listen(process.env.PORT, () => {});
