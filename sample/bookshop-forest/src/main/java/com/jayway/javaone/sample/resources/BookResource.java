package com.jayway.javaone.sample.resources;

import com.jayway.forest.legacy.constraint.DoNotDiscover;
import com.jayway.forest.legacy.roles.Resource;
import com.jayway.javaone.sample.constraints.HasBoughtBook;
import com.jayway.javaone.sample.domain.Book;
import com.jayway.javaone.sample.domain.CustomerRepository;

public class BookResource implements Resource {

	private final Book book;

    public BookResource(Book book) {
		this.book = book;
	}

    @DoNotDiscover
	public Book getBook() {
		return book;
	}

	@HasBoughtBook(false)
    public void buy() {
		CustomerRepository.getCurrent().bought(book);
    }

    @HasBoughtBook(true)
    public String download() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    }
}