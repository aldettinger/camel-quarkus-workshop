package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes /*TODO-FROM-CAMEL-QUARKUS-DOC*/ {
    @Override
    public void configure() {
        /*TODO-FROM-CAMEL-DOC*/("timer:myTimer")
                .setBody(constant("Transformed message content"))
                ./*TODO-FROM-CAMEL-DOC*/("log:myLogCategory");
    }
}
