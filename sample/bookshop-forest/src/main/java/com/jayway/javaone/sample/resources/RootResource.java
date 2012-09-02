package com.jayway.javaone.sample.resources;

import com.jayway.forest.legacy.roles.Resource;

public class RootResource implements Resource {

    public Resource books() {
        return new BooksResource();
    }
}
