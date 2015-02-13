package com.moandjiezana.currencyfair.markettradeprocessor;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

import java.nio.file.Paths;


public class Launcher {

  public static void main(String[] args) throws Exception {
    DeploymentManager manager = Servlets.defaultContainer()
      .addDeployment(
      Servlets.deployment()
        .setClassLoader(Launcher.class.getClassLoader())
        .setContextPath("/")
        .setDeploymentName("markettradeprocessor.war")
        .setResourceManager(new FileResourceManager(Paths.get("src", "main", "resources", "META-INF", "resources").toFile(), 1000000))
        .addWelcomePage("index.html")
        .addServlets(Servlets.servlet(MessagesServlet.class.getSimpleName(), MessagesServlet.class)
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
