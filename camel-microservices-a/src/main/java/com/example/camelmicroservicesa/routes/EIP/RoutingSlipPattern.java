package com.example.camelmicroservicesa.routes.EIP;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

// The "Routing Slip" is an Enterprise Integration Pattern (EIP) used in Apache Camel and other integration frameworks to dynamically determine the sequence of processing steps for a message at runtime.
// In a Routing Slip pattern, you specify a list of endpoints that a message should visit in a predefined order, but the actual sequence and order of visiting those endpoints are determined dynamically based on the message content or other runtime factors.
// This allows you to route a message through different processing steps based on the specific needs of the message.
//@Component
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
