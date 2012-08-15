package com.jayway;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class OrdersResource {
	@Path("{id}")
	public OrderResource order(@PathParam("id") String id) {
		return new OrderResource(id);
	}
}
