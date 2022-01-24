package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() {
from("platform-http:/cq-http-endpoint").setBody(constant("Hello Camel Quarkus in JVM mode from the 3h workshop room !"));    
}
}
