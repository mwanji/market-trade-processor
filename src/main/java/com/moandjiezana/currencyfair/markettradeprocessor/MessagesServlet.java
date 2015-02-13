package com.moandjiezana.currencyfair.markettradeprocessor;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(req.getInputStream()), JsonObject.class);
    String json = jsonObject.toString();
    
    MessagesEndpoint.sessions.get().forEach(ws -> {
      if (ws.isOpen()) {
        ws.getAsyncRemote().sendText(json);
      }
    });
    
    resp.setStatus(SC_OK);
  }
}
