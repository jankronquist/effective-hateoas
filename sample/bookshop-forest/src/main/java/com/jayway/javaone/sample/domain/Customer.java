package com.jayway.javaone.sample.domain;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Set<String> ownedBooks = new HashSet<String>();
	
	public boolean ownsBook(Book book) {
		return ownedBooks.contains(book.getId());
	}
	
	public void bought(Book book) {
		ownedBooks.add(book.getId());
	}
}
