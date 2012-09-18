package com.jayway.javaone.sample.resources;

import java.util.ArrayList;
import java.util.List;

import com.jayway.forest.legacy.exceptions.NotFoundException;
import com.jayway.forest.legacy.roles.IdDiscoverableResource;
import com.jayway.forest.legacy.roles.Linkable;
import com.jayway.forest.legacy.roles.Resource;
import com.jayway.javaone.sample.domain.Book;
import com.jayway.javaone.sample.domain.BookRepository;

public class BooksResource implements IdDiscoverableResource {
	
    @Override
    public List<Linkable> discover() {
        List<Linkable> links = new ArrayList<Linkable>();
        for (Book book : BookRepository.findAll()) {
            links.add( new Linkable(book.getId(), book.getTitle()) );
		}
        return links;
    }

    @Override
    public Resource id( String id ) {
    	Book book = BookRepository.get(id);
    	if (book == null) {
            throw new NotFoundException( "No such book '" + id + "'");
    	}
		return new BookResource(book);
    }
}
