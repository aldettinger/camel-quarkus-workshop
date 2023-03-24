package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        /**
         * This Camel Route simulates the external app that adds orders
         */
        from("timer:create-random-coffee-orders")
                .to("https:{{random-coffee-api}}")
                .unmarshal().json(Coffee.class)
                .bean("myBean", "generateOrder")
                .marshal().json()
                .to("kafka:orders");

        // This is the Kafka Consumer Route to implement
        from("kafka:orders")
                .log("received from kafka : ${body}")
                .unmarshal().json(CoffeeOrder.class)
                .to("jpa:"+CoffeeOrder.class)
                .bean("myBean", "generateNotification")
                .log("${body}")
                .to("slack://general")
        ;

        /**
         * REST api to fetch Coffee Orders
         */
        rest("order-api").description("Coffee Orders REST service")
                .get("/order").description("The list of all the coffee orders")
                .to("direct:orders-api")
                .get("/order/{id}").description("A Coffee order by id")
                .to("direct:order-api");

        from("direct:orders-api")
                .routeId("orders-api")
                .log("Received a message in route orders-api")
                .to("jpa:" + CoffeeOrder.class + "?namedQuery=findAll")
                .marshal().json();

        from("direct:order-api")
                .routeId("order-api")
                .log("Received a message in route order-api")
                /*Complete the route to fetch order by id*/
                .toD("jpa://" + CoffeeOrder.class.getName() + "?query=select m  from " + CoffeeOrder.class.getName() + " m  where id =${header.id}")
                .marshal().json()
        ;

    }
}
