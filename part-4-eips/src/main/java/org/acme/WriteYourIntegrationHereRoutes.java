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
            // aggregate
                .aggregate(header("importance"), new AggregationStrategy() {
                    @Override
                    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                        if (oldExchange == null) {
                            return newExchange;
                        }
                        Integer body1 = oldExchange.getIn().getBody(Integer.class);
                        Integer body2 = newExchange.getIn().getBody(Integer.class);

                        oldExchange.getIn().setBody(body1 + body2);
                        return oldExchange;
                    }
                })
                .completionSize(3)
            .to("direct:events-sink");
    }
}
