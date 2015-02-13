# Market Trade Processor

## WARNING

It was unclear what the date format in the trades' JSON is. I used `DD-MMM-YY HH:mm:ss`, but it could also be `YY-MMM-DD HH:mm:ss`.

## Flow

When a JSON object is POSTed to /message, it is lightly modified and broadcast to all open websockets.

When the websocket clients receive a message, they add it to the chart they are displaying.

## How to run

The server is written in Java 8 and requires Maven to build. The client is in JavaScript.

Execute the Launcher class. Then visit `http://localhost:8082`

## Libraries

[Undertow](http://undertow.io): small, embedabble, non-blocking server in Java based on Netty. Chosen because it is fast and low on resources.
[Gson](https://code.google.com/p/google-gson/): Java JSON library
[Highcharts](http://www.highcharts.com): JavaScript charts
[Moment.js](http://momentjs.com): JavaScript dates

## Unit test

There is a single unit test that starts the server and a standalone websocket client, POSTs to /messages and checks the message received by the websocket.

## Potential modifications

Streaming data: currently, a plain Java Servlet handles the POST and broadcasting. In the future, a framework such as Apache Spark or RxJava might handle the stream more robustly.

Charting: a specialised time series visualisation library based on D3 (such as [Cubism](https://square.github.io/cubism/)) might be better suited.
