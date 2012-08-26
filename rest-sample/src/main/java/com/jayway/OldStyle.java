package com.jayway;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class OldStyle {
	@Path("book") @GET
	public BooksDTO getBooks() {
		return null;
	}

	@Path("book/{isbn}") @GET
	public BookDTO getBook(@PathParam("isbn") String isbn) {
		return null;
	}

	@Path("buy") @POST
	public ReceiptDTO buy(@FormParam("isbn") String isbn) {
		return null;
	}
}
