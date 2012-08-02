package com.jayway;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("")
public class RootResource {
    private OrdersResource ordersResource = new OrdersResource();

    @GET 
    @Produces("text/plain")
    public String get() {
        return "Hello world";
    }

    @Path("orders")
    public OrdersResource orders() {
        return ordersResource;
    }
}
