package com.jayway.javaone.sample.resources;

import com.jayway.forest.legacy.constraint.DoNotDiscover;
import com.jayway.forest.legacy.exceptions.NotFoundException;
import com.jayway.forest.legacy.roles.IdDiscoverableResource;
import com.jayway.forest.legacy.roles.Linkable;
import com.jayway.forest.legacy.roles.Resource;

import java.util.ArrayList;
import java.util.List;

public class BooksResource implements IdDiscoverableResource {

    @Override
    public List<Linkable> discover() {
        List<Linkable> links = new ArrayList<Linkable>();
        links.add( new Linkable("hamlet") );
        links.add( new Linkable("macbeth") );
        return links;
    }

    @Override
    public Resource id( String id ) {
        if ( id.toLowerCase().equals( "hamlet" ) ||
             id.toLowerCase().equals("macbeth") ) {
            return new BookResource( id.toLowerCase() );
        }
        throw new NotFoundException( "No such book '" + id + "'");
    }
}
