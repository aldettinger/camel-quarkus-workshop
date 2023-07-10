package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {
    @Override
    public void configure() {
        from("timer:myTimer")
                .setBody(constant("Transformed message content"))
                .to("log:myLogCategory");
    }
}
