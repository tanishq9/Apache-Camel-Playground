package com.example.camelmicroservicesa.routes.EIP;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class MulticastPattern extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("timer:multicast-demo?period=10000")
				//.multicast()
				.to("log:something1", "log:something2");

		// The .multicast() component is used to explicitly define multicast processing. In this case, it doesn't change the behavior of your route because you are already sending the message to two endpoints concurrently.
		// The .multicast() component is more useful when you want to apply additional processing steps or aggregations on the results of multiple branches of processing before continuing in the route.
		// When the .multicast() component is commented out, the messages won't be sent concurrently to multiple endpoints, and they will be processed sequentially.
		// If `.multicast()` is uncommented then the messages will be sent concurrently to "log:something1" and "log:something2," allowing both endpoints to process the messages at the same time when the timer triggers.
	}
}
