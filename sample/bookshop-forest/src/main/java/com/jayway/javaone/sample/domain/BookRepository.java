package com.jayway.javaone.sample.domain;

import java.util.HashMap;
import java.util.Map;


public class BookRepository {

	private static Map<String, Book> books = new HashMap<String, Book>();
	
	static {
		add(new Book("123", "Hamlet", "William Shakespeare"));
		add(new Book("456", "Macbeth", "William Shakespeare"));
	}

	private static void add(Book book) {
		books.put(book.getId(), book);
	}

	public static Iterable<Book> findAll() {
		return books.values();
	}

	public static Book get(String id) {
		return books.get(id);
	}
}
