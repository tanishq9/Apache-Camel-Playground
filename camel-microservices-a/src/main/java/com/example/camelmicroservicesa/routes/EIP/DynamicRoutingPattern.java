package com.example.camelmicroservicesa.routes.EIP;

import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// The dynamicRouter() method takes as its argument a reference to a bean or a method that calculates the next routing destination. This method can be used to route messages based on dynamic criteria, external data, or any custom logic you implement. The method returns the next endpoint URI to which the message should be routed.
// In Apache Camel, the dynamicRouter() method will continue to execute until it returns null.
// It uses the return value of the dynamic routing method to determine the next destination for routing.
// As long as the method returns a non-null endpoint URI, the routing will continue (which can cause a recursive loop).
// When the processing of the current endpoint (the one returned by the method or bean) is complete, the method or bean is invoked again to determine the next routing destination.
// If the method or bean returns a non-null URI, the message is routed to that endpoint, and the processing continues with the corresponding route.
// Each step we invoke some business logic, decide what's the next step, execute that step, and then again execute the business logic to determine the next step
@Component
public class DynamicRoutingPattern extends RouteBuilder {

	@Autowired
	private DynamicRouterBean dynamicRouterBean;

	@Override
	public void configure() throws Exception {
		from("timer:dynamicRouting?period=10000")
				.transform().constant("My message")
				.dynamicRouter(method(dynamicRouterBean))
				.to("log:print-logs");

		from("direct:endpoint1")
				.log("Inside endpoint1")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						Thread.sleep(10000);
					}
				})
				.to("log:endpoint1log");

		from("direct:endpoint2")
				.log("Inside endpoint2")
				.to("log:endpoint2log");
	}
}

@Component
class DynamicRouterBean {
	int invocations = 0;

	String determineEndpointToInvoke(
			@Body String body,
			@ExchangeProperties Map<String, String> exchangeProperties
	) {
		invocations++;
		if (invocations == 1) {
			return "direct:endpoint1";
		} else if (invocations == 2) {
			return "direct:endpoint2";
		} else {
			return null;
		}
	}
}