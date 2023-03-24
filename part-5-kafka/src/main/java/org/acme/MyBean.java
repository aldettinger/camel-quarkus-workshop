package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@RegisterForReflection
@Named("myBean")
public class MyBean {

    public CoffeeOrder generateOrder(Coffee coffee){
        return new CoffeeOrder(coffee);
    }

    public String generateNotification(CoffeeOrder order) {
        return "ORGANIZER sending notification for new coffee order : " + order.getId();
    }
}
