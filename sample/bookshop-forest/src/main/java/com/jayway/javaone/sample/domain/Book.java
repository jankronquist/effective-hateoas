package com.jayway.javaone.sample.domain;

public class Book {
	private final String id;
	private final String title;
	private final String author;

	public Book(String id, String name, String author) {
		this.id = id;
		this.title = name;
		this.author = author;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}
}
