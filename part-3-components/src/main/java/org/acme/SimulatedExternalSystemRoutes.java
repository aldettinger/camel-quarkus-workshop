package org.acme;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.command.Message;
import org.apache.camel.builder.RouteBuilder;
import org.jboss.logging.Logger;

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
 * > A queue named "out-orders" on an ActiveMQ server configured with a non-default connector (tcp://localhost:61617)
 */
@ApplicationScoped
public class SimulatedExternalSystemRoutes extends RouteBuilder {

    private static final Logger LOGGER = Logger.getLogger(SimulatedExternalSystemRoutes.class);
    private BrokerService amqBroker;

    @PostConstruct
    void init() throws Exception {
        LOGGER.info("Starting ActiveMQ embedded broker");
        amqBroker = new BrokerService();
        amqBroker.setPersistent(false);
        amqBroker.addConnector("tcp://0.0.0.0:61617");
        amqBroker.setPlugins(new BrokerPlugin[] {new SimulatedTargetBrokerPlugin()});
        amqBroker.start();
    }

    @PreDestroy
    void destroy() {
        LOGGER.info("Stopping ActiveMQ embedded broker");
        try {
            amqBroker.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SimulatedTargetBrokerPlugin implements BrokerPlugin {

        @Override
        public Broker installPlugin(Broker broker) throws Exception {
            return new SimulatedTargetBroker(broker);
        }
    }

    public static class SimulatedTargetBroker extends BrokerFilter {

        public SimulatedTargetBroker(Broker next) {
            super(next);
        }

        @Override
        public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
            LOGGER.info("Destination system received a message via the Camel Quarkus ACTIVEMQ extension");
            super.send(producerExchange, messageSend);
        }
    }

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
