package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * Please write your integration logic here to apply some Enterprise Integration Patterns.
 */
@ApplicationScoped
public class WriteYourIntegrationHereRoutes extends RouteBuilder {
    @Override
    public void configure() {
        from("direct:events-source")
            // resequence
            .resequence(header("id")).batch()
            .to("direct:events-sink");
    }
}
