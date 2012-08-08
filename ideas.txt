
REST Intro:
-3-tier system
-what is a resource (uniform interface)
-what is a representation
-what is a link
-REST definition (-constraints, -pros/cons)
-Confusion of 'what is REST'
-Richardson maturity model


Construction:
-Classic JAX-RS style is broken
-URL structure should have a meaning reflected in code
  -sub resources
  -path as method name
  -single root resource
-Constraints on methods
-Link building statically ( new Link( root.product(17).purchase() )
-Link relation


Test:
-Simple to browse
-Javascript devs no need to know Java
-No need to maintain API docs
-HOW?
-Conventions for generic client
  -Provide generic client
     1. html representation (browser is generic client - cons: only GET/POST etc)
     2. javascript client
  -Pros/cons HTML representation: +straight forward, -incorrect verbs for PUT/DELETE, 
                          -different API use from normal client,
                          -not clear separation between normal and generic
  -Pros/Cons explicit generic client: +clear separation between normal and generic client
                                      -framework support, +operates the same way as the
                                      real client



Consumption:
-Consumption meaning: external client that cusomes the API!
-Discovery via state machine
-Knows parameter + media types + rels from documentation
-Classic: Boolean/enums replication of domain logic
-Links: control behaviour in client (e.g. what buttons to show)


Conclusion:

Practical 
-pros: 
       less repeated business logic in client => clients can handle change in logic
       test
       auto documentation
       
-cons: 
       Some overhead in code (missing framework)
       runtime overhead (evalutaion path + constraints)
       

