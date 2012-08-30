!SLIDE subsection
# DRAFT #

!SLIDE 
# Effective HATEOAS #
# with JAX-RS #

.notes The thing that programmers like about REST is the simplicity. So when architects start talking about hypermedia and all kinds of weird constraints many developers just stops listening. Too complex. Too much work. We want to show that hypermedia is also simple and that there are ways of minimizing the amount of work necessery. 

.notes REST is more than HTTP, but for the purposes of this presentation we will only consider HTTP.

!SLIDE bullets
# Example domain #

* A web shop for e-books
* Books - isbn, name, price, authors ...


!SLIDE bullets
# Uniform interface #

.notes The simplicity of REST comes from the uniform interface. Once you know this you can interact with any resource. For HTTP the uniform interface consists of:

    an initial line
    header lines
    blank line
    optional message body

.notes Constrast this to binary protocols where you can't see whats going on, or SOAP that is built on top of HTTP.

!SLIDE bullets
# Example request/response #

.notes AVAILABLE IN KEYNOTE

.notes The HTTP uniform interface defines a number of verbs and status codes with very specific semantics. For example a GET request cannot modify a resource. 

.notes The flexibility comes from identified resources and their representations. The verbs are designed to be efficient for large-grain hypermedia data transfer

GET /api/books/9780596805838

200 OK
Content-Type: application/json;charset=utf-8

{
  "isbn": "9780596805838",
  "title": "REST in Practice",
  "authors": [
    "Jim Webber",
    "Savas Parastatidis",
    "Ian Robinson"
  ],
  "publisher": "O'Reilly Media",
  "supplier_id": "ELIB",
  "price": "31.99",
  "language": "English",
  "imageURL": "/images/9780596805838",
  "downloadable_formats": [ 
    "EPUB",
    "PDF"
  ],
}

.notes When performing a HTTP request besides the verb you must also identify the resource. More about resource identifiers later on.

.notes The response can contain a representation of a the resource and the format of the representation is called a mediatype.

!SLIDE bullets incremental
# Resources are not domain objects #

* `/book/123` = a specific book
* `/book` = a collection of books
* `/book/123/buy` = identifies that the user wants to buy the book
* `/ownedbooks` = a collection of books that the user owns

.notes Anything that can be identified. document, person, todays weather. Very generic, more than just domain objects

!SLIDE bullets
# Richardson Maturity Model #

* Level 0
* Level 1 - Resources
* Level 2 - HTTP Verbs
* Level 3 - Hypermedia Controls

.notes Level 2 is CRUD services which is very useful, eg databases, S3. 

.notes Level 3 means that the resource representation contains links to other resources

!SLIDE small
# Hypermedia #

    @@@ xml
    <link rel="payment" href="http://example.org/product/123/buy"/>

    @@@ javascript
    "_links": {
        "payment": { 
          "href": "/product/123/buy", 
          "title": "Köp" 
        },
      },

.notes Hypermedia is just links, typically embedded in the representation. Besides the target URI (actually IRI) a link must include semantic information to allow automatic processing. This is done using the rel attribute that describes the relationship between the current resource and the target resource. Links can also include other attributes, for example "title" which is a human-readable identifier, "name" to distinguish between links with the same "rel".

.notes Since JAX-RS don't distinguish between trailing slash relative links must be built differently depending on how the resource was accessed (http://java.net/jira/browse/JAX_RS_SPEC-5)

!SLIDE bullets
# Level 3 example #

* TODO: Image showing a graph of related resources

.notes AVAILABLE IN KEYNOTE

.notes Go through an example where a client navigates through a set of resources using links to purchase and download a book.

.notes The application state is where in all this the client currently has navigated and where the client can go from here.

.notes The Hypermedia constraint means that client should only know the root URI and all state changes driven by links.

!SLIDE bullets
# Level 3 consequences #

* Clients don't care about URL structure
* Clients are DRY

.notes Since the clients only follow links they don't care about how the URLs are structured. When constructing the server we are free to chose a structure that fits our purposes.

.notes Clients don't have to repeat business logic. If a link is there it means the user can navigate the link. If a link is not there it means that the action is not available.

!SLIDE bullets
# The 3 tiers have evolved #

* TODO: Image

.notes A few years ago people thought of a web applications a 3 simple tiers (browser + web server + database). Now we often have different types of clients (web + mobile + integration?). Different backends (the whole NoSQL thing, integration with other systems). 
When designing our API we need to think about different clients. Complex backends with complex business logic or even complex non-functional behavior might be reflected in our API (eg long running transactions, authentication...)



!SLIDE subsection
#Part I: Construction


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

!SLIDE bullets
# Build your own framework

* Inspiration from project Streamflow
* Resource DSL 
* 1-1 reflection of resources and URL
* Single root resource
* Everything is linked from root
* Upon every invocation the path is evaluated through the resource tree

.notes We really felt these concepts were missing and it turned out no to be too hard to accomplish 

!SLIDE bullits
# Book Shop Demo

.notes show the book shop example. Show the code and show the HTML interface. See the resemblance and try invoking a command and explain the 405 roundtrip. Self-documentation should be mentioned.

!SLIDE 
# Root Resource

    @@@ java
    public class RootResource 
         implements Resource {
        
        public Resource books() {
            return new BooksResource();
        }
    }

!SLIDE 

    @@@ java
    public class BooksResource 
        implements CollectionsResource<Link> {
        
        public Resource item(String id) {
           return new BookResource( id );
        }
        
        public List<Link> items() {
           // lookup books from repository 
           return LinksBuilder.asList( books );
        }
    }

!SLIDE

    @@@ java
    public class BookResource
        implements BookResource {
             
        Book book;
        public BookResource( String id ) {
            // lookup particular book
            book = ...
            if ( book == null ) // throw 404
        }

        public void buy( PurchaseDTO dto ) {
            ...
        }

        public InputStream download() {...}
     }

!SLIDE 
# Browse it!

.notes Notice the enablement in the Book resource - you have to buy it before you can 
download it.

!SLIDE 
    @@@ java
    @HasBoughtBook(false)
    public void buy(PurchaseDTO dto) { ... }

    @HasBoughtBook(true)
    public InputStream download() {...}
    

!SLIDE bullets
# Framework advantages

* No mess in the resources
* POJO code because of conventions
* Discovery and documentation for free
* Hierarchical constraints easy

.notes Quite small framework to implements which turned very popular in our organization. We thought
we were done with REST but these conventions might be very handy and maybe even a prerequisite for
a REST framework but REST is more than URL conventions, namely hypermedia.

!SLIDE bullets
# Framework disadvantages

* No JAX-RS dependency so,
* Request/Response handling is re-implemented
* Media-type extension impossible

.notes This seems only solvable by combining JAX-RS 2.0 and Forest.

!SLIDE bullets
# REST level 3 Framework Should

* Enforce resources inter-linking
* Support notion of root resource
* Be mediatype flexible (easy to define own mediatype)
* Use the resource structure when mapping the URL to resource

.notes Interlinking is vastly overlooked in the REST community, yet being the key point in 
Fieldings dissertation: You must be able to consume a REST API without any other knowledge 
than generic understanding of standard mediatypes and a starting URL. 


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

!SLIDE small
# Classic JAX-RS #

    @@@ java
    @Path("book") @GET
    public BooksDTO getBooks() {}

    @Path("book/{isbn}") @GET
    public BookDTO getBook(@PathParam("isbn") String isbn) {}

    @Path("buy") @POST
    public ReceiptDTO buy(@FormParam("isbn") String isbn) {}
    
.notes Paths are created on an ad hoc basis, aiming for nice-looking URI:s that a client can understand.

!SLIDE small
# Hypermedia style JAX-RS #

    @@@ java
    @Path("")
    class RootResource {}
    class BooksResource {
      @GET
      public BooksDTO get() {}

      @Path("{isbn}")
      public BookResource item() {}
    }
    class BookResource {
      @GET
      public BookDTO get() {}
  		
      @Path("buy") @POST
      public ReceiptDTO buy() {}
    }

.notes Related functionality is grouped together.

.notes use child resources whenever a parent creates links to the child. Structure your URLs so that most links point to child resources. 80/20 rule

.notes TODO: How to motivate the deviation from standard JAX-RS? There are lots of things that probably don't work when using subresources... eg Link.fromResourceMethod

!SLIDE small
# Resource reflection

    @@@ java
    class BookResource {
      @GET
      public BookDTO get() {}
	
      @Path("buy") @POST
      @Rel("payment")
      public ReceiptDTO buy() {}
    }

.notes By using reflection we can generate relevant links. For example when handling a request to get a book we can generate a link to the buy resource. 

!SLIDE small
# Constraints

    @@@ java
    class BookResource {
      @Path("buy") @POST
      @Rel("payment")
      @HasBoughtBook(false)
      public ReceiptDTO buy() {}

      @Path("download") @GET
      @Rel("enclosure")
      @HasBoughtBook(true)
      public InputStream download() {}
    }

.notes Since we generate links by using reflection we can also add declarative constraints that specify if the link should be included or not. 

!SLIDE small
# Constraints and subresources

    @@@ java
    public class RootResource {
      @Path("admin")
      @HasPrivilege("administrator")
      public AdminResource admin() {
          return adminResource;
      }
    }

.notes Since we use subresources we can specify constraints on the methods for retrieving subresources and we can declarative control what resources are available!

.notes How to implement this using JAX-RS?

!SLIDE bullets incremental
# Framework support

.notes We have tried different approaches to get framework support the link generation and the constraints.

.notes We built our own webframework that gradually supported some parts of the JAX-RS standard. Initially this seemed to work quite well, but as the requirements for entity marshalling got more complex and more control of the communication was needed, we realized that it would be much better to re-use an existing implementation and allow underlying abstraction leak up to allow more control.

.notes The next approach was to use bytecode manipulation to add constraint validation, automatically add links and also add some methods that were needed

* Own webframework
* Bytecode manipulation
* JAX-RS 2.0


!SLIDE bullets
# Typesafe link building

* TODO
* new Link( root().product(17).purchase() )

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


