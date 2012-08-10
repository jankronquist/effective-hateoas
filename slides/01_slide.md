!SLIDE 
# Effective HATEOAS #
# with JAX-RS #

!SLIDE bullets
# REST #

* Representational state transfer
* Architectural style defined by Roy Fielding
* We will only consider HTTP

.notes 
http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm
In order to obtain a uniform interface, multiple architectural constraints are needed to guide the behavior of components. REST is defined by four interface constraints: identification of resources; manipulation of resources through representations; self-descriptive messages; and, hypermedia as the engine of application state. These constraints will be discussed in Section 5.2.

!SLIDE bullets
# Resources #

* Anything that can be identified

.notes document, person, todays weather. Very generic, more than just domain objects

!SLIDE bullets
# Representations #

* A sequence of bytes + metadata
* Data format is called media type
* Not the same thing as the resource

!SLIDE bullets
# Uniform interface #

* Verbs
* Status codes

.notes a few verbs and status codes with strict semantics. The flexibility comes from identified resources and their representations.
 
.notes designed to be efficient for large-grain hypermedia data transfer

!SLIDE bullets
# Links for machines #

* Pointer to a target resource
* Semantic information
* Optional metadata

.notes

!SLIDE bullets
# Richardson Maturity Model #

* Level 0
* Level 1 - Resources
* Level 2 - HTTP Verbs
* Level 3 - Hypermedia Controls

.notes Level 2 is CRUD services which is very useful, eg databases, wikis, S3. 

!SLIDE bullets
# The Hypermedia constraint #

* Client should only know the root URI
* All state changes driven by links

!SLIDE bullets
# The 3 tiers have evolved #

* 

.notes A few years ago people thought of a web applications a 3 simple tiers (browser + web server + database). Now we often have different types of clients (web + mobile + integration?). Different backends (the whole NoSQL thing, integration with other systems). 
When designing our API we need to think about different clients. Complex backends with complex business logic or even complex non-functional behavior might be reflected in our API (eg long running transactions, authentication...)



!SLIDE bullets
#Part I: Construction


!SLIDE bullets
#JAX-RS

* Provides basic protocol features
* Low level granularity
* Not much help developing REST level 3

.notes You can create a huge mess by just adding these annotation on your
classes and methods. Basically it is too general to be usable in developing
REST level 3. 

!SLIDE bullets
# URL Structure

* URL Structure should have a meaning reflected in the code
* A single root resource
* Sub resources 
* Path as method name

.notes This brings order to the resources. With these guideline implemented you cannot
"get lost" when browsing the API - it is always clear what method you are invoking or
which resource you are located in.

!SLIDE bullets
# Constraints On Methods

* A very convenient way of controlling enablement
* Annotations on methods, e.g. @ItemsInShoppingCart
* Hierarchical enforced constraint rules

.notes With the difinition of the resources in a resource tree, the constrains becomes
a way of enforcing the rules further down in the tree. 

!SLIDE bullets
# Link building

* Prefered is to have absolute links
* It should be easy
* Type safe: new Link( root().product(17).purchase() )

!SLIDE bullets
# Link Relation

* As a prerequisite links must define their semantic meaning
* Done in with the 'rel' attribute
* Describes the semantic relationship between the resource and what is linked

 


!SLIDE bullets
# Part II: TEST





!SLIDE bullets
# Just a transport mechanism #

* Rule 1 - Focus on what + why

.notes Uncle Bob - The web is just a transport mechanism. Focus on your application domain and business logic (why + what), not the how
x
!SLIDE bullets
# URL structure #

* Rule 2 - Structure your code around the URLs

.notes The URL structure is only relevant for the server developer. It often reflects the framework or how the server logic is structured. Client applications do not care!
Embrace this fact! Structure your code around the URLs


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
