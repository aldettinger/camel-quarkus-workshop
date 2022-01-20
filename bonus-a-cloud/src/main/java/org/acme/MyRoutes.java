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
                .to("kafka:orders");

        // This is the Kafka Route to create
        from("kafka:orders")
                .unmarshal().json(CoffeeOrder.class)
                .to("jpa:" + CoffeeOrder.class)
                .bean("myBean", "generateNotification")
                .to("slack://general?webhookUrl={{webhook-url}}");

        /**
         * REST api to fetch Coffee Orders
         */
        rest("order-api").description("Coffee Orders REST service")
                // REST endpoint to get all coffee orders using JPA NamedQuery
                .get("/order").description("The list of all the coffee orders")
                .route().routeId("orders-api")
                .to("jpa:" + CoffeeOrder.class + "?namedQuery=findAll")
                .marshal().json()
                .endRest()
                // REST endpoint to get coffee order by id using a JPA Query
                .get("/order/{id}").description("A Coffee order by id")
                .route().routeId("order-by-id")
                .toD("jpa://" + CoffeeOrder.class.getName() + "?query=select m  from " + CoffeeOrder.class.getName() + " m  where id =${header.id}")
                .marshal().json()
                .endRest();
    }
}
