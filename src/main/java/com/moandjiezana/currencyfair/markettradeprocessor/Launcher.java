package com.moandjiezana.currencyfair.markettradeprocessor;

import static io.undertow.servlet.Servlets.servlet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;


public class Launcher {

  public static void main(String[] args) throws Exception {
    DeploymentManager manager = Servlets.defaultContainer()
      .addDeployment(
      Servlets.deployment()
        .setClassLoader(Launcher.class.getClassLoader())
        .setContextPath("/")
        .setDeploymentName("markettradeprocessor.war")
        .setResourceManager(new ClassPathResourceManager(Thread.currentThread().getContextClassLoader(), "META-INF/resources"))
        .addWelcomePage("index.html")
        .addServlets(
          servlet(MessagesServlet.class.getSimpleName(), MessagesServlet.class)
            .addMapping("/messages"))
        .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
          new WebSocketDeploymentInfo()
            .addEndpoint(MessagesEndpoint.class))
    );
    
    manager.deploy();
      
    Undertow.builder()
      .addHttpListener(8082, "localhost")
      .setHandler(Handlers.path()
        .addPrefixPath("/", manager.start()))
      .build()
      .start();
  }
}
