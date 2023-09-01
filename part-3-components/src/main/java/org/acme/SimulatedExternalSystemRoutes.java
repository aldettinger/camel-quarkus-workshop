package org.acme;

import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * The purpose of this class is to simulate the source and target systems
 * 
 * The source information system is regularly:
 * > Producing files to the "target/in-orders" folder
 * > Producing files to the "target/in-orders-recursive/sub-folder" sub-folder
 *
 * The target information system is waiting for incoming messages into:
 * > A default HTTP server provided by Quarkus with default configuration (localhost:8080), the resourceUri is "out-orders"
 * > A folder named "target/out-orders"
 */
@ApplicationScoped
public class SimulatedExternalSystemRoutes extends RouteBuilder {

    @Override
    public void configure() {
        // source information system
        from("timer:sourceSystemProduceFileSteadily?period=10000")
                .setBody(constant("Produced with via the Camel Quarkus FILE extension"))
                .to("file:target/in-orders/");
        from("timer:sourceSystemProduceFileInSubFolderSteadily?period=10000")
                .setBody(constant("Produced with via the Camel Quarkus FILE extension in a subfolder"))
                .to("file:target/in-orders-recursive/sub-folder");

        // destination information system
        from("platform-http:/out-orders")
                .log("Target system received a message via the Camel Quarkus PLATFORM-HTTP extension");
        from("file:target/out-orders/?recursive=true")
                .log("Target system received a message via the Camel Quarkus FILE extension");
    }
}
