### Camel Terminology
Camel Context - (0..n) Routes + Components + ...
- Endpoint - Reference to a queue, db or a file
- Route -  Endpoints + Processor(s) + Transformer(s)
- Components - Extensions (Kafka, JSON, JMS, etc)
- Transformation
  - Data format transformation - XML to JSON
  - Data type transformation - String to CurrencyConversionBean
- Message - Body + Headers + Attachments
  - This message flows through the route
- Exchange - Request + Response
  - This exchange contains information about the input and output message
  - Exchange ID
  - Message Exchange Pattern (MEP)  - InOnly/InOut
  - Input Message and (Optional) Output Message

Note: from, to, log, bean, etc. is camel domain specific language (dsl) which makes enterprise integration very easy.

### Code Snippets

#### from and to 
from & to endpoints are often different because they represent the source & destination of the message flow in your route.
```
public class MyFirstTimerRouter extends RouteBuilder {
 
  @Override
  public void configure() throws Exception {
   // in configure method, we will create the route
   // camel is an integration framework
 
   // from("timer:first-timer") // timer
   //  .to("log:first-timer"); // log
 
   // timer will generate the below message every second
   // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
 
   from("timer:first-timer") // timer
     //.transform().constant("My constant message")
     .transform().constant("Time now is: " + LocalDateTime.now()) // the constant created here will be used throughout
     .to("log:first-timer22"); // log
 
   // Route: route1 started and consuming from: timer://first-timer
   // Total 1 routes, of which 1 are started
   // Exchange[ExchangePattern: InOnly, BodyType: String, Body: My constant message]
  }
}
```

#### Transformation and Processing
Instead of hardcoding the bean name in the route, we can autowire the bean for transformation class, and use that instead.
When we have 1 method in transformation bean class then we can skip specifying the name of method but we have >1 methods in your bean, then we need to mention which method of bean we would want to invoke.

There are 2 types of operations that we can do in a specific route.
- Processing: Operation which doesn't change body of the message.
- Transformation: Operation that changes the body of the message.

Since String is returned by bean method so message body would be changed to whatever is returned from this method.
If return type is void then it means we are doing some kind of processing, no change in message body.

```
// Used for transforming the message body
@Component
class GetCurrentTimeBean {
  // Since String is returned so message body would be changed to whatever is returned from this method
  public String getCurrentTime() {
   return "Time now is: " + LocalDateTime.now();
  }
}
 
// Used for processing the message body
@Component
class SimpleLoggingProcessingComponent {
  // This method doesn't need to return anything back
  // Since it is void it means we are doing some kind of processing
  public void process(String body) {
   System.out.println("Logging the message body: " + body);
  }
}
```

An alternative to using bean for processing (by returning void from bean method), is to make use of .process() method.

#### Using ActiveMQ
- Microservice A will log message to ActiveMQ and Microservice B will read that message.
- Various versions of ActiveMQ neatly packet into Docker images: https://hub.docker.com/r/rmohr/activemq
- docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
- The JMX broker listens on port 61616 and the Web Console on port 8161.
- Create a queue on ActiveMQ console started on localhost:8161.

#### timer and log types
- For the timer and log types, these endpoints are already defined by camel.
- By using Component architecture, Camel requires us to ONLY import the dependencies that we need like activemq, we won't need to bring in other dependencies, and therefore the microservice remains lightweight.
- Components are extensions used to add extra functionality.
- Good documentation regarding using various camel components: https://camel.apache.org/components/4.0.x/activemq-component.html

Note: Kafka commands to describe kafka topic and consumer group

```
kafka-topics.sh --bootstrap-server localhost:9092 --topic myKafkaTopic --describe
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group <group-name> --describe
```

#### Send REST call
```
@Override
public void configure() throws Exception {
  restConfiguration().host("localhost").port(8000);
  from("timer:rest-api-consumer?period=10000")
    .setHeader("from", () -> "USD") // setting the message header, can be used later in route
    .setHeader("to", () -> "INR")
    .log("${body}")
    .to("rest:get:/currency-exchange/{from}/{to}")
    .log("${body}"); // http call response body
}
```

#### Using simple language
- Using simple language, we can use keywords like ends, contains, starts, etc.
- https://camel.apache.org/components/4.0.x/languages/simple-language.html
- https://camel.apache.org/components/4.0.x/languages/file-language.html

#### Reusable routes
- The "direct" component in Apache Camel is used to create a direct, synchronous route or endpoint.
- It's often used for in-memory routing, allowing you to call one route directly from another within the same CamelContext without going through external systems or communication channels.
- The "direct" component is commonly used to define sub-routes or as a way to modularize and organize Camel routes.

```
from("direct:start")
    .setBody(constant("Input data")) // Set the input data
    .log("Received a message on 'start'")
    .to("direct:processData");
 
from("direct:processData")
    .log("Processing data: ${body}") // Log the input data
    .to("log:info");
```

#### Using producerTemplate
- The ProducerTemplate is a component of the Apache Camel framework that provides a programmatic way to send messages and trigger routes within your Camel context.
- It allows you to produce or send messages to different Camel endpoints, making it a valuable tool for interacting with and controlling your Camel routes programmatically.
```
ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
producerTemplate.sendBody("direct:start", "Hello, Camel!");
```
- While components like ActiveMQ and Kafka are used for messaging systems to send and receive messages, the direct component is not an external messaging system but rather a lightweight, in-memory, point-to-point communication mechanism within your Camel application.

#### Choice
- We can create a bean incase we need to validate some complex condition inside choice().
- In the decision beans or any beans we create, we can actually map specific values from the message or the exchange and use them to make decisions.
```
@Component
public class ComplexChoiceRouter extends RouteBuilder {
 
  @Autowired
  private DeciderBean deciderBean;
 
  @Override
  public void configure() throws Exception {
   from("file:files/input")
     .routeId("files-input")
     .transform().body(String.class)
     .choice()
      .when(method(deciderBean))
       .log("Condition met")
     .end()
     .to("log:print-logs");
  }
}
 
@Component
class DeciderBean {
  // assume this method performs complex conditional logic
  public boolean isConditionMet(
    @Body String body,
    @Headers Map<String, Object> headers,
    @ExchangeProperties Map<String, Object> properties
  ) {
   System.out.println("Message body is: " + body);
   System.out.println("Message headers is: " + headers);
   System.out.println("Properties is: " + headers);
   return true;
  }
}
```

### Enterprise Integration Routing Patterns
- https://camel.apache.org/components/4.0.x/eips/enterprise-integration-patterns.html

#### Multicast Pattern
```
@Component
public class MulticastPattern extends RouteBuilder {
  @Override
  public void configure() throws Exception {
   from("timer:multicast-demo?period=10000")
     //.multicast()
     .to("log:something1", "log:something2");
 
   // The .multicast() component is used to explicitly define multicast processing. In this case, it doesn't change the behavior of your route because you are already sending the message to two endpoints concurrently.
   // The .multicast() component is more useful when you want to apply additional processing steps or aggregations on the results of multiple branches of processing before continuing in the route.
   // When the .multicast() component is commented out, the messages won't be sent concurrently to multiple endpoints, and they will be processed sequentially.
   // If `.multicast()` is uncommented then the messages will be sent concurrently to "log:something1" and "log:something2,"
  }
}
```

#### Splitter Pattern
- In Apache Camel, a "split pattern" refers to a design pattern used to split a message into multiple parts, often to process each part independently.
- This pattern is particularly useful when dealing with messages that contain collections or batches of data and you need to process each element of the collection individually.

```
@Component
public class SplitPattern extends RouteBuilder {
  @Override
  public void configure() throws Exception {
   from("file:files/csv")
     .unmarshal().csv()
     .split(body())
     .to("log:split-files");
  }
}
```
- More ways to split covered in below snippet:
```
@Component
public class SplitPattern extends RouteBuilder {
 
  @Autowired
  SplitterComponent splitterComponent;
 
  @Override
  public void configure() throws Exception {
   from("file:files/csv")
     .convertBodyTo(String.class)
     // .transform().body(String.class)
     //.unmarshal().csv()
     //.split(body()) --> First way to split (line is delimiter)
 
     //.split(body(), ",") --> Second way to split using ',' i.e. custom delimiter
 
     .split(method(splitterComponent)) // --> Third way is to use custom splitter, here in we can write custom logic to split
     .to("log:split-files");
  }
}
 
@Component
class SplitterComponent {
  public List<String> splitInput(String body) {
   // Assume some custom logic was executed on message body and the below list was returned as an outcome
   return List.of("ABC", "DEF", "GHI");
  }
}
```

#### Rolling Slip Pattern
- The Routing Slip pattern is commonly used in scenarios where you need to dynamically determine the sequence of processing steps for messages, such as in workflow orchestration or for implementing routing decisions based on message content or conditions.
- It provides a powerful way to define dynamic and adaptive processing sequences in your integration solutions.

```
public class RoutingSlipPattern extends RouteBuilder {
  @Override
  public void configure() throws Exception {
   // The below routing slip determines the order of steps/endpoints to be invoked
   // Assume it determined on runtime via business logic
   String dynamicRoutingSlip = "direct:endpoint1,direct:endpoint2";
 
   from("timer:routingSlipDemo?period=10000")
     .transform().constant("My message is hardcoded")
     .routingSlip(simple(dynamicRoutingSlip));
 
   from("direct:endpoint1")
     .to("log:endpoint1log");
 
   from("direct:endpoint2")
     .to("log:endpoint2log");
  }
}
```
- The "Routing Slip" is an Enterprise Integration Pattern (EIP) used in Apache Camel and other integration frameworks to dynamically determine the sequence of processing steps for a message at runtime.
- In a Routing Slip pattern, you specify a list of endpoints that a message should visit in a predefined order, but the actual sequence and order of visiting those endpoints are determined dynamically based on the message content or other runtime factors.
- This allows you to route a message through different processing steps based on the specific needs of the message.

#### Dynamic Router Pattern
- The dynamicRouter() method takes as its argument a reference to a bean or a method that calculates the next routing destination. This method can be used to route messages based on dynamic criteria, external data, or any custom logic you implement. The method returns the next endpoint URI to which the message should be routed.
- In Apache Camel, the dynamicRouter() method will continue to execute until it returns null. It uses the return value of the dynamic routing method to determine the next destination for routing. As long as the method returns a non-null endpoint URI, the routing will continue (which can cause a recursive loop).
- When the processing of the current endpoint (the one returned by the method or bean) is complete, the method or bean is invoked again to determine the next routing destination. If the method or bean returns a non-null URI, the message is routed to that endpoint, and the processing continues with the corresponding route.

Summary: Each step we invoke some business logic (inside bean provided to dynamicRouter), decide what's the next step, execute that step, and then again execute the business logic (inside bean provided to dynamicRouter) to determine the next step.

Some references -
- https://camel.apache.org/components/4.0.x/eips/enterprise-integration-patterns.html
- https://camel.apache.org/components/4.0.x/index.html
- https://camel.apache.org/camel-spring-boot/4.0.x/list.html
- https://camel.apache.org/camel-spring-boot/4.0.x/spring-boot.html
- https://camel.apache.org/manual/faq/why-the-name-camel.html
