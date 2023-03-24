package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() {
from("platform-http:/cq-http-endpoint").setBody(constant("Hello Camel Quarkus in NATIVE mode from the workshop room !"));    
}
}
