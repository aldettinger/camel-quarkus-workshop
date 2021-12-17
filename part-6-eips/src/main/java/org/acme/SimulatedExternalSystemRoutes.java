package org.acme;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class SimulatedExternalSystemRoutes extends RouteBuilder {

    private static int count = 0;

    @Override
    public void configure() {
        // Each 3s, fires a group of 3 events
        from("timer:produceEventsSteadily?fixedRate=true&period=3000")
                .split(simple("[1,2,3]"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) {
                        int importance = (count+1)%3+1;
                        exchange.getMessage().setHeader("importance", importance);
                        exchange.getMessage().setHeader("id", count-1+importance);
                        exchange.getMessage().setBody(importance*10);
                        count++;
                    }})
                .to("direct:events-source");

        from("direct:events-sink").log("Received event with headers[id=${header.id}, importance=${header.importance}] AND body[${body}]");
    }
}

