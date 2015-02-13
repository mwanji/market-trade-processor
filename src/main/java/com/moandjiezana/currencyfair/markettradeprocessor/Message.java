package com.moandjiezana.currencyfair.markettradeprocessor;

public class Message {

  static enum Type {
    VOLUME;
  }
  
  public final Message.Type type;
  public final Object payload;
  
  public Message(Type type, Object payload) {
    this.type = type;
    this.payload = payload;
  }
  
}
