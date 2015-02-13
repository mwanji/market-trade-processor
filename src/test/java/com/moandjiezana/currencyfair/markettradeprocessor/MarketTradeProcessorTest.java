package com.moandjiezana.currencyfair.markettradeprocessor;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MarketTradeProcessorTest {

  @Test
  public void should_receive_post_and_write_to_websocket() throws Exception {
    Launcher.main(new String[0]);
    WebSocketClient client = new WebSocketClient();
    String originalPayload = "{\"userId\": \"134256\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\", \"amountSell\": 1000, \"amountBuy\": 747.10, \"rate\": 0.7471, \"timePlaced\" : \"14-JAN-15 10:27:44\", \"originatingCountry\" : \"FR\"}";
    
    try {
      client.start();
      ClientUpgradeRequest request = new ClientUpgradeRequest();
      MessageSocket websocket = new MessageSocket();
      Future<Message> future = websocket.get();
      client.connect(websocket, new URI("ws://localhost:8082/ws/messages"), request);
      
      HttpRequest.post("http://localhost:8082/messages")
        .acceptJson()
        .send(originalPayload)
        .code();
      
      Message message = future.get(15, TimeUnit.SECONDS);
      
      assertEquals(Message.Type.VOLUME, message.type);
      Gson gson = new Gson();
      JsonObject payload = gson.fromJson(gson.toJson(message.payload), JsonObject.class);
      
      assertEquals(gson.fromJson(originalPayload, JsonObject.class), payload);
    } finally {
      client.stop();
    }
  }
  
  @WebSocket
  public class MessageSocket {
    private final CompletableFuture<Message> futureMessage = new CompletableFuture<>();
    
    Future<Message> get() {
      return futureMessage;
    }
    
    @OnWebSocketMessage
    public void onMessage(String jsonString) {
      futureMessage.complete(new Gson().fromJson(jsonString, Message.class));
    }
  }
}
