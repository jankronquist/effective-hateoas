package com.jayway.javaone.sample.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jayway.forest.legacy.constraint.Constraint;
import com.jayway.forest.legacy.constraint.ConstraintEvaluator;
import com.jayway.javaone.sample.domain.CustomerRepository;
import com.jayway.javaone.sample.resources.BookResource;

/**
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(HasBoughtBook.Evaluator.class)
public @interface HasBoughtBook {

    boolean value();

    class Evaluator implements ConstraintEvaluator<HasBoughtBook, BookResource> {
    	
        public boolean isValid( HasBoughtBook annotation, BookResource resource) {
            return annotation.value() == CustomerRepository.getCurrent().ownsBook(resource.getBook());
        }

    }

}
