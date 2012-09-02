package com.jayway.javaone.sample.domain;

public class CustomerRepository {
	private static Customer customer = new Customer();
	
	public static Customer getCurrent() {
		return customer;
	}
}
