package com.moandjiezana.currencyfair.markettradeprocessor;

import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.util.Methods;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.io.IOException;

public class Launcher {

  public static void main(String[] args) {
    HttpHandler websocketHandler = websocket((WebSocketHttpExchange exchange, WebSocketChannel channel) -> {
      channel.getReceiveSetter().set(new AbstractReceiveListener() {
        @Override
        protected void onFullTextMessage(final WebSocketChannel channel, BufferedTextMessage message) throws IOException {
          WebSockets.sendText(message.getData(), channel, null);  
        }
      });
      channel.resumeReceives();
    });
    
    HttpHandler messagesHandler = Handlers.predicate(exchange -> exchange.getRequestMethod().equals(Methods.GET), websocketHandler, exchange -> {});

    HttpHandler rootHandler = Handlers.path()
      .addExactPath("/", resource(new ClassPathResourceManager(Launcher.class.getClassLoader(), Launcher.class.getPackage())).addWelcomeFiles("index.html"))
      .addExactPath("/messages", messagesHandler);
      
    Undertow.builder()
      .addHttpListener(8082, "localhost")
      .setHandler(rootHandler)
      .build()
      .start();
  }
}
