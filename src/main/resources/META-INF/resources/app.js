"use strict";

var APP = {
  Trade: function () {
    var amountSell, rate;
    amountSell = Math.random() * (5000 - 10) + 10;
    rate = 0.7471;

    return {
      "userId": "134256",
      "currencyFrom": "EUR",
      "currencyTo": "GBP",
      "amountSell": amountSell,
      "amountBuy": amountSell * rate,
      "rate": rate,
      "timePlaced" : moment().format("DD-MMM-YY HH:mm:ss").toUpperCase(),
      "originatingCountry" : "FR"
    };
  },
  sendTrade: function (trade) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/messages', true);
    xhr.send(JSON.stringify(trade));
  }
};

(function () {
  var socket, volumeChart, volumeTotalAmount = 0;

  if (window.WebSocket) {
      socket = new WebSocket("ws://localhost:8082/ws/messages");
      socket.onmessage = function(event) {
        var json = JSON.parse(event.data);
        var filteredSeries;
        console.log(new Date() + " Received data from websocket: " + json);

        if (json.type === "VOLUME") {
          filteredSeries = volumeChart.series.filter(function (s) { return s.name === json.payload.currencyFrom });
          if (filteredSeries.length === 0) {
            filteredSeries.push(volumeChart.addSeries({
              name: json.payload.currencyFrom
            }));
          }
          filteredSeries.forEach(function (s) {
            volumeTotalAmount += json.payload.amountSell
            s.addPoint([moment(json.payload.timePlaced, "DD-MMM-YY HH:mm:ss").valueOf(), volumeTotalAmount]);
          });
        }

      };

      socket.onopen = function(event) {
        console.log("Web Socket opened!");
      };
      socket.onclose = function(event) {
        alert("Web Socket closed.");
      };
  } else {
    alert("Your browser does not support Websockets. Please use one that does.");
  }

  document.getElementById("postTradeTrigger").addEventListener("click", function () {
    APP.sendTrade(APP.Trade());
  });

  volumeChart = new Highcharts.Chart({
    title: {
      text: "Total amount by currency"
    },
    xAxis: {
      type: "datetime"
    },
    chart: {
      renderTo: document.getElementById("volumeChartContainer"),
    },
    series: []
  });
}())
