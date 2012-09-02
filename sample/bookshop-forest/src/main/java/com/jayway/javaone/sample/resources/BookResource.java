package com.jayway.javaone.sample.resources;

import com.jayway.forest.legacy.constraint.DoNotDiscover;
import com.jayway.forest.legacy.roles.Resource;
import com.jayway.javaone.sample.constraints.HasBoughtBook;

public class BookResource implements Resource {

    private String id;

    @DoNotDiscover
    public String getId() {
        return id;
    }


    public BookResource( String id ) {
        this.id = id;
    }

    @HasBoughtBook(true)
    public void buy() {

    }

    @HasBoughtBook(false)
    public String download() {
        return "content";
    }
}
