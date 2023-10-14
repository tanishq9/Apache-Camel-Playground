package com.example.camelmicroservicesa.routes.L2;

import java.time.LocalDateTime;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class UsingBeanForTransformation extends RouteBuilder {

	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;

	@Autowired
	private SimpleLoggingProcessingComponent simpleLoggingProcessingComponent;

	@Override
	public void configure() throws Exception {
		// in configure method, we will create the route
		// camel is an integration framework
		from("timer:first-timer") // timer

				// Message body transformation
				.log("${body}") // logging the message body before transformation
				//.bean(getCurrentTimeBean, "getCurrentTimeAnother") // When we have 1 method in transformation bean class then we can skip specifying the name of method but we have >1 methods in your bean, then we need to mention which method of bean we would want to invoke.
				.bean(getCurrentTimeBean, "getCurrentTimeAnotherWithParam(${body})")
				.log("${body}") // logging the message body after transformation
				//.bean("getCurrentTimeBean") // Instead of hardcoding the bean name in the route, we can autowire the bean for transformation class, and use that instead.

				// Message body processing
				.bean(simpleLoggingProcessingComponent, "process")
				.log("${body}") // Should be same as earlier, as above bean is for processing not transforming
				.to("log:first-timer"); // log
	}
}

// Used for transforming the message body
@Component
class GetCurrentTimeBean {
	// Since String is returned so message body would be changed to whatever is returned from this method
	public String getCurrentTime() {
		return "Time now is: " + LocalDateTime.now();
	}

	public String getCurrentTimeAnother() {
		return "Time now is: " + LocalDateTime.now();
	}

	public String getCurrentTimeAnotherWithParam(String body) {
		System.out.println("Body is: " + body);
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
