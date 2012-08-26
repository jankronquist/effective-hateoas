package com.jayway;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public class NewStyle {
	class RootResource {
		@Path("book")
		public BooksResource book() {
			return null;
		}
	}

	class BooksResource {
		@GET
		public BooksDTO get() {
			return null;
		}
		@Path("{isbn}")
		public BookResource item() {
			return null;
		}
	}
	
	class BookResource {
		@GET
		public BookDTO get() {
			return null;
		}
		@Path("buy") @POST
		public ReceiptDTO buy() {
			return null;
		}
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
