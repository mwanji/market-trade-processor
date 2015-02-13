package com.moandjiezana.currencyfair.markettradeprocessor;

import io.undertow.Undertow;

public class Launcher {

  public static void main(String[] args) {
    Undertow.builder()
      .addHttpListener(8082, "localhost")
      .setHandler(exchange -> exchange.getResponseSender().send("Hello"))
      .build()
      .start();
  }
}
