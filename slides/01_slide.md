!SLIDE subsection
# DRAFT #

!SLIDE 
# Effective HATEOAS #
# with JAX-RS #

!SLIDE 
# Practitian vs Professor #

.notes The thing that programmers like about REST is the simplicity. So when architects start talking about hypermedia and all kinds of weird constraints many developers just stops listening. Too complex. Too much work. We want to show that hypermedia is also simple and that there are ways of minimizing the amount of work necessery. 

.notes REST is more than HTTP, but for the purposes of this presentation we will only consider HTTP.

!SLIDE bullets
# Our background #

* Several JAX-RS applications
* Qi4j and Streamflow
* Forest

.notes Qi4j is a Java framework with a completely different programming model than traditional Java which aims to make Domain Driven Design possible to implement in Java. In Qi4j the REST API is exposing the possible interactions with the domain as resources. Each interaction becomes a separate resource. The problem with Qi4j is that it is completely different from all other Java code, which makes it hard to find people that wants to learn it. We wanted to take the ideas around REST in Qi4j and implement them using a mainstream Java programming model. The result was a framework we call Forest, which have been successfully used by several projects.

!SLIDE subsection
# Hypermedia API:s #

!SLIDE bullets
# Example domain #

* A web shop for e-books
* properties: isbn, name, price, authors ...

.notes There is no shopping cart. The application should also run on mobile devices.

!SLIDE bullets incremental
# Interactions #

* View book details
* Query and browse for books
* Buy book
* Download book

.notes Interactions is what a client can do in our application. The resources are the interactions exposed for a web client.

!SLIDE bullets incremental
# CRUD resources #

* `<type>/<id>`
* `PUT, GET, PATCH, DELETE /book/123`
* `POST, GET /book`

!SLIDE bullets incremental
# Resources are not domain objects #

* `GET /book/123/details`
* `GET /book/123/download`
* `POST /book/123/buy`
* `GET /owned`

.notes Anything that can be identified. document, person, todays weather. Very generic, more than just domain objects

!SLIDE bullets
# Richardson Maturity Model #

* Level 2 - HTTP Verbs
* Level 1 - Resources
* Level 0

.notes Level 2 is CRUD services which is very useful, eg databases, S3. 

!SLIDE small
# Current JAX-RS #

    @@@ java
    // Resources
    @Path("myresource")
    
    // Verbs
    @GET @POST @DELETE
    
    // Status codes
    return Response.status(Status.CONFLICT);
    throw new WebApplicationException(...)


!SLIDE bullets smaller
# Level 3 - Hypermedia Controls #

    XML
    <link rel="payment" href="http://example.org/product/123/buy"/>

    JSON
    "_links": {
        "payment": { 
          "href": "/product/123/buy", 
          "title": "Köp" 
        },
      },

.notes Level 3 means that the resource representation contains links to other resources

.notes Hypermedia is just links, typically embedded in the representation. Besides the target URI (actually IRI) a link must include semantic information to allow automatic processing. This is done using the rel attribute that describes the relationship between the current resource and the target resource. Links can also include other attributes, for example "title" which is a human-readable identifier, "name" to distinguish between links with the same "rel".

!SLIDE bullets
# Level 3 example #

* AVAILABLE IN KEYNOTE

.notes 

.notes Go through an example where a client navigates through a set of resources using links to purchase and download a book.

.notes The application state is where in all this the client currently has navigated and where the client can go from here.

.notes The Hypermedia constraint means that client should only know the root URI and all state changes driven by links.

!SLIDE subsection
# Example using Forest #


!SLIDE bullets
# Book Shop Demo

.notes show the book shop example. Show the code and show the HTML interface. See the resemblance and try invoking a command and explain the 405 roundtrip. Self-documentation should be mentioned.

!SLIDE 
# Root Resource

    @@@ java
    public class RootResource 
         implements Resource {
        
        public Resource book() {
            return new BooksResource();
        }
    }

!SLIDE 
# Collection resource 

    @@@ java
    public class BooksResource 
        implements CollectionsResource<Link> {
        
        public Resource item(String id) {
           return new BookResource( id );
        }
        
        public List<Link> discover() {
           // lookup books from repository 
           return LinksBuilder.asList( books );
        }
    }

!SLIDE
# Book resource

    @@@ java
    public class BookResource
        implements Resource {
             
        private Book book;
        public BookResource( String id ) {
            book = // lookup book
            if ( book == null ) // throw 404
        }

        public void buy(PinDTO pin) {...}

        public InputStream download() {...}
     }

!SLIDE 
# Constraint problem

     @@@ java
     public void buy(PinDTO pin) {
       if (hasBoughtBook()) {
         throw new WebApplicationException(...);
       }
     }


!SLIDE 
# Declarative constraints

    @@@ java
    @HasBoughtBook(false)
    public void buy(PinDTO pin) { ... }

    @HasBoughtBook(true)
    public InputStream download() {...}
    

!SLIDE bullets
# Forest advantages

* Discovery and documentation for free
* Conventions resulted in clean code 
* Declarative constraints

.notes Quite small framework to implements which turned very popular in our organization. We thought
we were done with REST but these conventions might be very handy and maybe even a prerequisite for
a REST framework but REST is more than URL conventions, namely hypermedia.


!SLIDE bullets
# Forest disadvantages

* Very basic HTTP support
* Very limited control for developer

.notes No support for mediatypes, headers. Only child linking easy. Rel attribute not supported well enough.

!SLIDE bullets
# Forest conclusion

* Great experiment
* Served us well
* Not general purpose

.notes The framework served our purposes very well and we could live with these limitations

!SLIDE subsection
# JAX-RS level 3? #

!SLIDE bullets
# JAX-RS 2.0 #

    @@@ java
    public final class Link {
      public URI getUri() {}
      public String getTitle() {}
      public String getRel() {}
      ...
    }

.notes JAX-RS 2.0 adds the Link class. Is this enough to create hypermedia API:s? ??? No! If the programmer is responsible for adding links everywhere this will not be used as it creates massive boilerplate. We think a framework is needed that minimizes the amout of links the programmer has to create manually. All the resources in the application must be accessible via links without the programmerer having to write a single line of code.

!SLIDE bullets
# Framework requirements #

* Single root resource
* Rel attribute support
* Sensible default links
* Declarative constraints

.notes All the resources in the application must be accessible via links without the programmerer having to write a single line of code.

!SLIDE
# Only a single Root Resource #

    @@@ java
    @Path("")
    public class RootResource {
      @Path("book")
      public BooksResource book() {
          return booksResource;
      }
    }

.notes REST APIs should have a single wellknown URL. Reflect this by having a single JAX-RS root resource

!SLIDE
# Rel annotation #

    @@@ java
    class BookResource {
      @Path("buy") @POST
      @Rel("payment")
      public ReceiptDTO buy(@FormParam("pin") String pin) { ... }
    }

!SLIDE
# Sensible default links #

    @@@ java
    public class BookResource {
      @GET
      public Response get() {
        return Response.ok(toDTO(book))
          .build();
      }
      @Path("buy") 
      @POST
      @Rel("payment")
      public ReceiptDTO buy(@FormParam("pin") String pin) { ... }
    }

!SLIDE
# Use reflection #

    @@@ java
    public class BookResource {
      @GET
      public Response get() {
        return Response.ok(toDTO(book))
          .links(Framework.makeLinks(this))
          .build();
      }
      @Path("buy") 
      @POST
      @Rel("payment")
      public ReceiptDTO buy(@FormParam("pin") String pin) { ... }
    }

!SLIDE
# ContainerResponseFilter #

    @@@ java
    public interface ContainerResponseFilter {
      public void filter(
            ContainerRequestContext requestContext, 
            ContainerResponseContext responseContext)
              throws IOException;
    }

.notes Links can be generated automatically using reflection on the target resource. Always include links to all resource-methods in the resource class.

!SLIDE
# Declarative constraints #

    @@@ java
    public class BookResource {
      @Path("buy") 
      @POST
      @Rel("payment")
      @HasBoughtBook(false)
      public ReceiptDTO buy(@FormParam("pin") String pin) { ... }
    }

!SLIDE
# ContainerRequestFilter #

    @@@ java
    public interface ContainerRequestFilter {
      public void filter(ContainerRequestContext requestContext) throws IOException;
    }

.notes Constraints can be enforced when the request is performed
    
!SLIDE bullets
# Summary

* Framework needed on top of JAX-RS
* Declarative constraints needed
* Sensible default links

!SLIDE bullets
# Questions

!SLIDE bullets
# Use absolute links #

* JAX\_RS_SPEC-5

.notes Since JAX-RS don't distinguish between trailing slash relative links must be built differently depending on how the resource was accessed (http://java.net/jira/browse/JAX_RS_SPEC-5)


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


!SLIDE subsection
#JAX-RS problems and framework motivation


!SLIDE bullets
#JAX-RS

* Using JAX-RS 1.1
* Provides basic protocol features
* Great API, very flexible


.notes Great API which is easy to use but quite low level

!SLIDE 
# JAX-RS Example

    @@@ java
    @Path("somepath")
    public class MyResource {
        @PATH("/other/path/dosomething")
        @POST 
        @Produces("application/json")
        public String doSomething() {
            return "Hello world";
        }
    }

.notes Not much help developing REST level 3. You can create a huge mess by just adding these annotation on your
classes and methods. Basically it is too general to be usable in developing
REST level 3. 


!SLIDE bullets
#JAX-RS challenges

* How do you link things together?
* How do you provide meaning to the resource structure?
* How do you add restrictions in your resources (admin rights etc)? Or just basic control flow?

.notes JAX-RS and it's @Path is really just a relay mechanism, so you have no help in linking 
your resources together which is a REST primer. Using bare bone JAX-RS the hypermedia constraint 
becomes a bigger burden than the benefits. 

!SLIDE bullets small
# Build your own framework

* Inspiration from project Streamflow
* Resource DSL 
* 1-1 reflection of resources and URL
* Single root resource
* Everything is linked from root
* Upon every invocation the path is evaluated through the resource tree

.notes We really felt these concepts were missing and it turned out no to be too hard to accomplish 


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

!SLIDE bullets incremental
# Framework support

.notes We have tried different approaches to get framework support the link generation and the constraints.

.notes We built our own webframework that gradually supported some parts of the JAX-RS standard. Initially this seemed to work quite well, but as the requirements for entity marshalling got more complex and more control of the communication was needed, we realized that it would be much better to re-use an existing implementation and allow underlying abstraction leak up to allow more control.

.notes The next approach was to use bytecode manipulation to add constraint validation, automatically add links and also add some methods that were needed

* Own webframework
* Bytecode manipulation
* JAX-RS 2.0

!SLIDE bullets
# Uniform interface #

.notes The simplicity of REST comes from the uniform interface. Once you know this you can interact with any resource. For HTTP the uniform interface consists of:

    an initial line
    header lines
    blank line
    optional message body
