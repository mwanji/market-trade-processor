"use strict";

var trade = {
  "userId": "134256",
  "currencyFrom": "EUR",
  "currencyTo": "GBP",
  "amountSell": 1000,
  "amountBuy": 747.10,
  "rate": 0.7471,
  "timePlaced" : "14-JAN-15 10:27:44",
  "originatingCountry" : "FR"
};

var socket;
if (window.WebSocket) {
    socket = new WebSocket("ws://localhost:8082/ws/messages");
    socket.onmessage = function(event) {
      console.log(new Date() + " Received data from websocket: " + event.data);
    };
    socket.onopen = function(event) {
      alert("Web Socket opened!");
    };
    socket.onclose = function(event) {
      alert("Web Socket closed.");
    };
} else {
  alert("Your browser does not support Websockets. Please use one that does.");
}

function send(message) {
  if (!window.WebSocket) {
  return;
  }
  if (socket.readyState == WebSocket.OPEN) {
    socket.send(message);
  } else {
    alert("The socket is not open.");
  }
}

document.getElementById("postTradeTrigger").addEventListener("click", function () {
  var xhr = new XMLHttpRequest();
  xhr.open('POST', '/messages', true);
  xhr.send(JSON.stringify(trade));
});
