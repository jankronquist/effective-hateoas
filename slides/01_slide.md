!SLIDE subsection
# DRAFT #

!SLIDE 
# Effective HATEOAS #
# with JAX-RS #

!SLIDE bullets
# REST #

* Representational state transfer
* Architectural style defined by Roy Fielding
* We will only consider HTTP

.notes http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm
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

* TODO: Image

.notes A few years ago people thought of a web applications a 3 simple tiers (browser + web server + database). Now we often have different types of clients (web + mobile + integration?). Different backends (the whole NoSQL thing, integration with other systems). 
When designing our API we need to think about different clients. Complex backends with complex business logic or even complex non-functional behavior might be reflected in our API (eg long running transactions, authentication...)



!SLIDE subsection
#Part I: Construction


!SLIDE bullets
#JAX-RS

* Provides basic protocol features
* Great API, very flexible
* TODO: example

.notes Not much help developing REST level 3

.notes You can create a huge mess by just adding these annotation on your
classes and methods. Basically it is too general to be usable in developing
REST level 3. 

!SLIDE bullets
# URLs

* Level 3 clients should not care
* Menaningful only for server developer

.notes Since the clients only follow links, the format of the actual target URLs are not relevant
The URL structure is only relevant for the server developer. It often reflects the framework or how the server logic is structured. Client applications do not care!
Embrace this fact! Structure your code around the URLs

!SLIDE bullets
# URL recommendations

* Meaning reflected in the code
* A single root resource
* Sub resources 
* Path as method name

.notes This brings order to the resources. With these guideline implemented you cannot
"get lost" when browsing the API - it is always clear what method you are invoking or
which resource you are located in.

!SLIDE
# Only a single Root Resource #

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
# Path as method name #

    @@@ java
    @Path("orders")
    public OrdersResource orders() {
        return ordersResource;
    }


!SLIDE small
# Sub resource #

    @@@ java
  	@Path("{id}")
  	public OrderResource order(@PathParam("id") String id) {
  		return new OrderResource(id);
  	}

!SLIDE bullets
# URL Structure

* Make most links point to child resources

.notes use child resources whenever a parent creates links to the child. Structure your URLs so that most links point to child resources. 80/20 rule

!SLIDE bullets
# URL examples

* `/` links to `/product`
* `/product` links to `/product/123`
* `/product/123` links to `/product/123/purchase`

!SLIDE bullets
# Link building

* Prefer absolute links
* It should be easy

.notes Since JAX-RS don't distinguish between trailing slash relative links must be built differently depending on how the resource was accessed
http://java.net/jira/browse/JAX_RS_SPEC-5

!SLIDE bullets
# Typesafe link building

* TODO
* new Link( root().product(17).purchase() )

!SLIDE bullets
# Link semantics

* <link rel="subresource" href="/api/orders">
* Done in with the 'rel' attribute
* Describes the relationship between the resource and target

!SLIDE bullets
# Resource reflection

* TODO

!SLIDE bullets
# Constraints On Methods

* A very convenient way of controlling enablement
* Annotations on methods, e.g. @HasItemsInShoppingCart
* Hierarchical enforced constraint rules

.notes With the definition of the resources in a resource tree, the constrains becomes
a way of enforcing the rules further down in the tree. 

!SLIDE subsection
# Part II: TEST

!SLIDE bullets
# Simplicity

* Simple to browse
* No need to maintain API documentation
* Javascript developers don't need to ask or inspect code

.notes Really big advantage to have a self-documenting API. We added @Doc("") to
further add some documentation to the API.

!SLIDE bullets
# Genric Client - HTML

* The browser is the generic client (prefered media type is text/html)
* Any resource can be rendered in browser
* Looks like ugly web site

!SLIDE bullets
# Pros/Cons HTML

* +straight forward
* +works in lynx
* -incorrect verbs for PUT and DELETE
* -different API than a real mediatype
* -unclear separation between normal and generic client

.notes This is the only one we have used and it is very useful. But the
incorrect verbs can lead to misuse of the API - we have seen javascript client
revert to text/html "to make it work"

!SLIDE bullets
# Generic Client - Javascript

* The browser is the generic client
* The root resource returns generic javascript client
* Only the root resource can be rendered in a browser

.notes Since media type "text/html" is not supported only the root resource
can be rendered in the browser. The root resource handles the serving of the 
generic client and functions as the starting point of the interaction.

!SLIDE bullets
# Pros/Cons Javascript

* +clear separation between normal and generic client
* +operates the correct way, i.e. no overloading of verbs
* -needs framework support 

.notes This is a better way for a generic client. You can clearly see the status codes
and input/output values. You always use the correct verbs. BUT the framework needs to 
have a options such that links are described fully: rel, href, method, parameters. Otherwise
the client wouldn't know how to invoke methods.


!SLIDE subsection
# Part III: Consumption


!SLIDE bullets
# Consumption definition

* External client consuming the API
* Client is NOT when using mediatype text/html on the API

.notes It is common in the CRUD REST frameworks that the REST API IS the client. It seems like a really bad idea to mix your API with presentation logic. And there may not be a 1-1 relation
between a resource and a view.


!SLIDE bullets
# A Client

* Generic knowledge: how to recognize a link
* Documented knowledge: parameters + media types + rels
* Root URL

.notes the generic knowledge is how to parse the discovered resources from the API, mainly 
how to recognize links. The documented knowledge is about how a specific method is supposed
to be invoked - e.g. what parameter, what method.

!SLIDES bullets 
# State Machine

* Client acts like a state machine
* Discovery on the API
* Based on the discovery presents what to do next for the user

!SLIDE bullets
# A Step Back

* Classic (and level 2) approach is to let the client know:
* All href for methods
* All server side state rules

.notes In this case the client must replicate all server side logic to know which operation is enabled for a given state. 

!SLIDE bullets
# A Step Forward

* When leveraging level 3 simply base your client on what you discover
* e.g. If you don't see the logout link, don't display the logout button

.notes When doing this the client focus becomes handling the presentation. That is REST is like a detail in the client code. 


!SLIDE bullets
# Conslusion

* Pros: 
* Less repeated business logic in client, meaning client can handle change in logic
* Very easy to test
* Auto documentation
* Cons: 
* Runtime overhead
* Overhead in producing code (missing framework)

.notes Having written both level 2 and level 3 clients, there is no mistake - level 3 is vastly simpler. But the framework is really missing. The benefit of producing level 3 does not obviously outweigh the benefits you get - that is probably the reason why level 3 REST is not implemented widely. 


!SLIDE bullets
# Questions



