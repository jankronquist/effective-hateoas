!SLIDE 
# Effective HATEOAS #
# with JAX-RS #

!SLIDE bullets
# Just a transport mechanism #

* Rule 1 - Focus on what + why

.notes Uncle Bob - The web is just a transport mechanism. Focus on your application domain and business logic (why + what), not the how

!SLIDE bullets
# URL structure #

* Rule 2 - Structure your code around the URLs

.notes The URL structure is only relevant for the server developer. It often reflects the framework or how the server logic is structured. Client applications do not care!
Embrace this fact! Structure your code around the URLs

!SLIDE bullets
# The 3 tiers have evolved #

* 

.notes A few years ago people thought of a web applications a 3 simple tiers (browser + web server + database). Now we often have different types of clients (web + mobile + integration?). Different backends (the whole NoSQL thing, integration with other systems). 
When designing our API we need to think about different clients. Complex backends with complex business logic or even complex non-functional behavior might be reflected in our API (eg long running transactions, authentication...)

!SLIDE bullets
# Rule 3 - Only a single Root Resource #

    @@@ java
    @Path("")
    public class RootResource {
        @GET 
        @Produces("text/plain")
        public String get() {
            return "Hello world";
        }
    }

.notes REST APIs should have a single wellknown URL. Reflect this by having a single JAX-RS root resource

!SLIDE
# Rule 4 - Path as method name #

    @@@ java
    @Path("orders")
    public OrdersResource orders() {
        return ordersResource;
    }

!SLIDE bullets
# Why links #

* HATEOAS is all about caring and guiding your clients

.notes I had a discussion with a friend who is working as an iPhone developer. It went somthing like this:
Him: "We are using REST for all our backend services! It is soo great!" 
Me: "Cool! Are you adhering to the hypermedia constraint?"
Him: "No, we don't see the point in using links. The server developers just document what URLs we should use. We use booleans and various other state to determine what to do."
Me: "What kind of problems are you having?"
Him: "The main problem we have is that the server guys don't really care what we are trying to accomplish in the UI. They just publish resources that they think is useful and we have to retrieve a bunch of resources to create a meaningful view. And the other day it turned out that this boolean had to be changed into an enum, which of course affected what resources to retrieve."


!SLIDE

TODO: examples

.notes 

------------------------------

TODO:
* What is a resource? Difference between resources and domain objects
* Commads vs queries
* Subresources, rels
