package com.jayway.javaone.sample.domain;

public class Book {
	private final String id;
	private final String name;
	private final String author;

	public Book(String id, String name, String author) {
		this.id = id;
		this.name = name;
		this.author = author;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}
}
