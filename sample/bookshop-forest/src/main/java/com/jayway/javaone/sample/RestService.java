package com.jayway.javaone.sample;

import com.jayway.forest.legacy.core.Application;
import com.jayway.forest.legacy.di.grove.GroveDependencyInjectionImpl;
import com.jayway.forest.legacy.roles.Resource;
import com.jayway.forest.legacy.servlet.RestfulServlet;
import com.jayway.javaone.sample.resources.RootResource;

public class RestService extends RestfulServlet {

	public void init() {
		initForest(new Application() {

            @Override
            public Resource root() {
                return new RootResource();
            }

            @Override
            public void setupRequestContext() {
            }

        }, new GroveDependencyInjectionImpl());
	}

}
