package com.moandjiezana.currencyfair.markettradeprocessor;

import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/messages")
public class MessagesEndpoint {
  
  static final AtomicReference<Set<Session>> sessions = new AtomicReference<>(emptySet());
  
  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    sessions.set(session.getOpenSessions());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    session.getAsyncRemote().sendText("Received: " + message);
  }
  
  @OnClose
  public void onClose(Session session) {
    sessions.set(session.getOpenSessions());
  }
}
