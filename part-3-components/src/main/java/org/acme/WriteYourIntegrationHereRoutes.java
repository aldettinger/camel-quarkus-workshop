package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

/**
 * Please write your integration logic here to transfer messages from the source
 * system to the target system.
 */
@ApplicationScoped
public class WriteYourIntegrationHereRoutes extends RouteBuilder {
    @Override
    public void configure() {
        from("file:target/in-orders-recursive/?recursive=true")
                .to("activemq:out-orders");
    }
}
