package com.jayway.javaone.sample.constraints;

import com.jayway.forest.legacy.constraint.Constraint;
import com.jayway.forest.legacy.constraint.ConstraintEvaluator;
import com.jayway.forest.legacy.roles.Resource;
import com.jayway.javaone.sample.resources.BookResource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(HasBoughtBook.Evaluator.class)
public @interface HasBoughtBook {

    boolean value();

    class Evaluator implements ConstraintEvaluator<HasBoughtBook, Resource> {

        public boolean isValid( HasBoughtBook role, Resource resource) {
            BookResource book = (BookResource) resource;
            return role.value() == book.getId().equals( "hamlet" );
        }

    }

}
