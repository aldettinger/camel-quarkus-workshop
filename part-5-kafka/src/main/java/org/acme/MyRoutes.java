package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /**
         * This Camel Route simulates the external app that adds orders
         */
        from("timer:create-random-coffee-orders")
                .to("http:{{random-coffee-api}}")
                .unmarshal().json(Coffee.class)
                .bean("myBean", "generateOrder")
                .marshal().json()
                /* TODO push the result to kafka*/;

        // This is the Kafka Consumer Route to implement
        from("kafka:orders")
                .log("received from kafka : ${body}");

        /**
         * REST api to fetch Coffee Orders
         */
        rest("order-api").description("Coffee Orders REST service")
                // REST endpoint to get all coffee orders using JPA NamedQuery
                .get("/order").description("The list of all the coffee orders")
                .route().routeId("orders-all")
                .to("jpa:" + CoffeeOrder.class + "?namedQuery=findAll")
                .marshal().json()
                .endRest()
                // TODO add new GET endpoint
                /*.get("/order/{id}").description("A Coffee order by id")
                .route().routeId("order-by-id")
                ....
                .endRest()
                 */
        ;

    }
}
